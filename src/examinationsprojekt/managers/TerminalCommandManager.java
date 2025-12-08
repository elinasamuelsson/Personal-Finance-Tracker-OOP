package examinationsprojekt.managers;

import examinationsprojekt.commands.*;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.ViewOptions;
import examinationsprojekt.repositories.AccountFileRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class TerminalCommandManager implements ICommandManager {
    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        System.out.println("Welcome to the revised OOP version of the Personal Finance Tracker!");

        while (true) {
            printMainMenuOptions();

            String userInput = input.stringInput();
            ICommand command = getMainMenuCommand(userInput);

            if (command != null && !userInput.equals("5")) {
                command.run();
            } else if (userInput.equals("5")) { //move validation into viewTransactionsCommand
                IAccountRepository repository = new AccountFileRepository();
                Account accountToView = null;

                if (CurrentStateManager.getCurrentAccount() == null) {
                    System.out.println("Select an account before viewing account balance.");
                    continue;
                }

                List<Account> accounts = repository.findAll();
                for (Account account : accounts) {
                    if (account.getName().equals(CurrentStateManager.getCurrentAccount().getName())) {
                        accountToView = account;
                    }
                }

                if (accountToView == null) {
                    System.out.println("Select an account before viewing transactions.");
                } else {
                    viewTransactionsSubMenu();
                }
            } else if (userInput.equals("0")) {
                System.out.println("Exiting program.");
                System.exit(0);
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }

    private void viewTransactionsSubMenu() {
        ICommand command;
        String userInput = "";
        while (true) {
            boolean viewEarning = userWantsToViewEarning();

            printViewTransactionsMenuOptions();
            userInput = input.stringInput();
            command = getViewTransactionsMenuCommand(userInput, viewEarning);

            if (command != null) {
                command.run();
            } else if (userInput.equals("0")) {
                return;
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }

    private void printMainMenuOptions() {
        List<String> menuOptionsDescriptions = new ArrayList<>();
        try {
            menuOptionsDescriptions = findAllCommandClassDescriptions(findAllCommandClasses());
        } catch (Exception e) {
            System.out.println("Menu options not found.");
        }

        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Please select an option from the following:");

        int index = 1;
        for (String description : menuOptionsDescriptions) {
            System.out.println("\t" + index + ". " + description);
            index++;
        }

        System.out.println("\t0. Exit program");
        System.out.println("-------------------------------------------");
        System.out.println();
    }

    private List<Class<?>> findAllCommandClasses() throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("examinationsprojekt/commands");

        List<Class<?>> commandClasses = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File dir = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));

            if (!dir.exists() || !dir.isDirectory()) {
                System.err.println("Skipping non-directory resource: " + resource);
                continue;
            }

            File[] files = dir.listFiles();
            if (files == null) continue;

            for (File file : files) {
                if (file.getName().endsWith(".class") && !file.getName().contains("ICommand")) {
                    String className = "examinationsprojekt.commands."
                            + file.getName().substring(0, file.getName().length() - 6);
                    commandClasses.add(Class.forName(className));
                }
            }
        }

        return commandClasses.stream()
                .sorted((a, b) -> {
                    try {
                        Object objA = a.getDeclaredConstructor().newInstance();
                        Object objB = b.getDeclaredConstructor().newInstance();
                        Field fieldA = a.getDeclaredField("index");
                        Field fieldB = b.getDeclaredField("index");
                        fieldA.setAccessible(true);
                        fieldB.setAccessible(true);
                        int indexA = (int) fieldA.get(objA);
                        int indexB = (int) fieldB.get(objB);
                        return Integer.compare(indexA, indexB);
                    } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException |
                             InvocationTargetException | InstantiationException | NoSuchMethodException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .toList();
    }

    private List<String> findAllCommandClassDescriptions(List<Class<?>> commandClasses) {
        List<String> commandClassesDescriptions = commandClasses.stream()
                .map(a -> {
                    try {
                        Object object = a.getDeclaredConstructor().newInstance();
                        Field description = a.getDeclaredField("description");
                        description.setAccessible(true);
                        return String.valueOf(description.get(object));
                    } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException |
                             InstantiationException | InvocationTargetException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .collect(Collectors.toList());
        return commandClassesDescriptions;
    }

    private ICommand getMainMenuCommand(String userInput) {
        int userChoice = 0;
        try {
            userChoice = Integer.parseInt(userInput);
        } catch (IndexOutOfBoundsException | NumberFormatException _) {
        }
        List<Class<?>> commandClasses;
        ICommand command = null;
        try {
            commandClasses = findAllCommandClasses();
        } catch (ClassNotFoundException | IOException exception) {
            throw new RuntimeException(exception);
        }

        try {
            return (ICommand) commandClasses.get(userChoice - 1).getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException | ArrayIndexOutOfBoundsException _) {
        }
        return command;
    }

    private void printEarningOrSpendingMenuOptions() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Please select an option from the following:");
        System.out.println("\t1. View earnings");
        System.out.println("\t2. View spending");
        System.out.println("\t0. Return to main menu");
        System.out.println("-------------------------------------------");
        System.out.println();
    }

    private boolean userWantsToViewEarning() {
        String userInput = "";

        while (true) {
            printEarningOrSpendingMenuOptions();

            userInput = input.stringInput();

            if (userInput.equals("1")) {
                return true;
            } else if (userInput.equals("2")) {
                return false;
            } else if (userInput.equals("0")) {
                run();
            }
        }
    }

    private void printViewTransactionsMenuOptions() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Please select an option from the following:");
        for (ViewOptions viewOption : ViewOptions.values()) {
            System.out.println("\t" + viewOption.getIndex() + ". " + viewOption.getDescription());
        }
        System.out.println("\t0. Return to earnings or spending");
        System.out.println("-------------------------------------------");
        System.out.println();
    }

    private ICommand getViewTransactionsMenuCommand(String userInput, boolean viewEarning) {
        return switch (userInput) {
            case "1" -> new ViewTransactionsCommand(ViewOptions.YEARLY, viewEarning);
            case "2" -> new ViewTransactionsCommand(ViewOptions.MONTHLY, viewEarning);
            case "3" -> new ViewTransactionsCommand(ViewOptions.WEEKLY, viewEarning);
            case "4" -> new ViewTransactionsCommand(ViewOptions.DAILY, viewEarning);
            case "5" -> new ViewTransactionsCommand(ViewOptions.CATEGORY, viewEarning);
            default -> null;
        };
    }
}
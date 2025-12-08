package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.models.TransactionTypes;
import examinationsprojekt.models.ViewOptions;
import examinationsprojekt.repositories.AccountFileRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

public class ViewTransactionsCommand implements ICommand {
    private final int index = 5;
    private final String description = "View transactions";
    private ViewOptions option;
    private boolean viewEarning;

    public ViewTransactionsCommand() {
    }

    public ViewTransactionsCommand(ViewOptions option, boolean viewEarning) {
        this.option = option;
        this.viewEarning = viewEarning;
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IAccountRepository repository = new AccountFileRepository();
        Account accountToView = null;

        List<Account> accounts = repository.findAll();
        for (Account account : accounts) {
            if (account.getName().equals(CurrentStateManager.getCurrentAccount().getName())) {
                accountToView = account;
            }
        }

        List<Transaction> transactions = sortAllTransactions(accountToView); //cannot be null, validation happens outside ViewTransactionCommand for now

        if (option.equals(ViewOptions.YEARLY)) {
            viewYearly(transactions);
        } else if (option.equals(ViewOptions.MONTHLY)) {
            viewMonthly(transactions);
        } else if (option.equals(ViewOptions.WEEKLY)) {
            viewWeekly(transactions);
        } else if (option.equals(ViewOptions.DAILY)) {
            viewDaily(transactions);
        } else if (option.equals(ViewOptions.CATEGORY)) {
            viewCategory(transactions);
        }
    }

    private void viewYearly(List<Transaction> transactions) {
        int dateToPrint = LocalDateTime.now().getYear();

        while (true) {
            System.out.println("----------------" + dateToPrint + "----------------");
            System.out.println();
            for (Transaction transaction : transactions) {
                if (transaction.getLocalTime().getYear() == dateToPrint) {
                    printTransaction(transaction);
                }
            }

            System.out.println("What do you want to do?");
            System.out.println("1. View previous year");
            System.out.println("2. View next year");
            System.out.println("3. Manually select year");
            System.out.println("0. Return");

            String userInput = input.stringInput();
            switch (userInput) {
                case "1":
                    dateToPrint--;
                    break;
                case "2":
                    dateToPrint++;
                    break;
                case "3":
                    try {
                        System.out.println("Enter the year you wish to view: ");
                        dateToPrint = input.intInput();
                    } catch (InputMismatchException exception) {
                        System.out.println("Please enter a valid year.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("This option does not exist. Try again.");
            }
        }
    }

    private void viewMonthly(List<Transaction> transactions) {
        LocalDateTime dateToPrint = LocalDateTime.now();
        String userInput = "";

        while (true) {
            System.out.println("----------------" + dateToPrint.getMonth().toString() + " " +
                    dateToPrint.getYear() + "----------------");
            System.out.println();
            for (Transaction transaction : transactions) {
                if (transaction.getLocalTime().getMonthValue() == dateToPrint.getMonthValue()
                        && transaction.getLocalTime().getYear() == dateToPrint.getYear()) {
                    printTransaction(transaction);
                }
            }

            System.out.println("What do you wish to do?");
            System.out.println("1. View previous month");
            System.out.println("2. View next month");
            System.out.println("3. Manually select month");
            System.out.println("0. Return");

            userInput = input.stringInput();
            switch (userInput) {
                case "1":
                    dateToPrint = dateToPrint.minusMonths(1);
                    break;
                case "2":
                    dateToPrint = dateToPrint.plusMonths(1);
                    break;
                case "3":
                    try {
                        System.out.println("Enter date of the day you wish to view: ");
                        System.out.println("Please use the format YYYY-MM");
                        String dateInput = input.stringInput();

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        dateToPrint = LocalDateTime.parse((dateInput + "-01 12:00"), formatter);
                    } catch (DateTimeParseException exception) {
                        System.out.println("Please enter a valid date.");
                        System.out.println("Please use the format YYYY-MM");

                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("This option does not exist. Try again.");
            }
        }
    }

    private void viewWeekly(List<Transaction> transactions) {
        WeekFields week = WeekFields.ISO;
        LocalDateTime dateToPrint = LocalDateTime.now()
                .with(week.dayOfWeek(), 1);

        transactions.sort(
                Comparator.comparingInt((Transaction transaction) -> transaction.getLocalTime().get(week.weekBasedYear()))
                        .thenComparing(transaction -> transaction.getLocalTime().get(week.weekOfWeekBasedYear())));

        String userInput = "";
        while (true) {
            System.out.println("----------------" + dateToPrint.get(week.weekOfWeekBasedYear()) + " " +
                    dateToPrint.get(week.weekBasedYear()) + "----------------");
            System.out.println();
            for (Transaction transaction : transactions) {
                if (transaction.getLocalTime().get(week.weekBasedYear()) == dateToPrint.get(week.weekBasedYear())
                        && transaction.getLocalTime().get(week.weekOfWeekBasedYear()) == dateToPrint.get(week.weekOfWeekBasedYear())) {
                    printTransaction(transaction);
                }
            }

            System.out.println("What do you wish to do?");
            System.out.println("1. View previous week");
            System.out.println("2. View next week");
            System.out.println("3. Manually select week");
            System.out.println("0. Return");

            userInput = input.stringInput();
            switch (userInput) {
                case "1":
                    dateToPrint = dateToPrint.minusDays(7);
                    break;
                case "2":
                    dateToPrint = dateToPrint.plusDays(7);
                    break;
                case "3":
                    try {
                        int weekInput = 0;
                        int yearInput = 0;

                        System.out.println("Enter the week you wish to view: ");
                        weekInput = input.intInput();

                        System.out.println("Enter the year of the week you wish to view: ");
                        yearInput = input.intInput();


                        dateToPrint = LocalDateTime.of(yearInput, 1, 1, 12, 0)
                                .with(week.weekOfWeekBasedYear(), weekInput)
                                .with(week.dayOfWeek(), 1);
                    } catch (InputMismatchException exception) {
                        System.out.println("Please enter a valid week and year.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("This option does not exist. Try again.");
            }
        }
    }

    private void viewDaily(List<Transaction> transactions) {
        LocalDateTime dateToPrint = LocalDateTime.now();

        String userInput = "";
        while (true) {
            System.out.println("----------------" + dateToPrint.getDayOfMonth() + " " +
                    dateToPrint.getMonth() + " " + dateToPrint.getYear() + "----------------");
            System.out.println();
            for (Transaction transaction : transactions) {
                if (transaction.getLocalTime().getDayOfYear() == dateToPrint.getDayOfYear()
                        && transaction.getLocalTime().getYear() == dateToPrint.getYear()) {
                    printTransaction(transaction);
                }
            }

            System.out.println("What do you wish to do?");
            System.out.println("1. View previous day");
            System.out.println("2. View next day");
            System.out.println("3. Manually select day");
            System.out.println("0. Return");

            userInput = input.stringInput();
            switch (userInput) {
                case "1":
                    dateToPrint = dateToPrint.minusDays(1);
                    break;
                case "2":
                    dateToPrint = dateToPrint.plusDays(1);
                    break;
                case "3":
                    try {
                        System.out.println("Enter date of the day you wish to view: ");
                        System.out.println("Please use the format YYYY-MM-DD");
                        String dateInput = input.stringInput();


                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        dateToPrint = LocalDateTime.parse((dateInput + " 12:00"), formatter);
                        break;
                    } catch (DateTimeParseException exception) {
                        System.out.println("Invalid date format. Try again.");
                        System.out.println("Please use the format YYYY-MM-DD");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("This option does not exist. Try again.");
            }
        }
    }

    private void viewCategory(List<Transaction> transactions) {
        String categoryToPrint = "";
        while (true) {
            try {
                System.out.println("Which category do you wish to view?");
                for (TransactionTypes type : TransactionTypes.values()) {
                    System.out.println(type.name());
                }
                System.out.println("To return type 'return'.");

                categoryToPrint = input.stringInput().toUpperCase();

                if (categoryToPrint.equals("RETURN")) {
                    return;
                }

                TransactionTypes chosenCategory = TransactionTypes.valueOf(categoryToPrint);

                System.out.println("----------------" + chosenCategory.getTypeDescription() + "----------------");
                System.out.println();
                for (Transaction transaction : transactions) {
                    if (transaction.getType() == chosenCategory) {
                        printTransaction(transaction);
                    }
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid category. Try again.");
            }
        }
    }

    private List<Transaction> sortAllTransactions(Account account) {
        List<Transaction> sortedTransactions = new ArrayList<Transaction>();

        for (Transaction transaction : account.getTransactionsCopy()) {
            if (viewEarning && transaction.isEarning()) {
                sortedTransactions.add(transaction);
            } else if (!viewEarning && !transaction.isEarning()) {
                sortedTransactions.add(transaction);
            }
        }

        sortedTransactions.sort(
                Comparator.comparingInt((Transaction transaction) -> transaction.getLocalTime().getYear()).reversed()
                        .thenComparing(transaction -> transaction.getLocalTime().getMonthValue())
                        .thenComparing(transaction -> transaction.getLocalTime().getDayOfYear()));

        return sortedTransactions;
    }

    private void printTransaction(Transaction transaction) {
        System.out.println("-------------------------------------------");
        System.out.print("Time: \t \t \t" + transaction.getLocalTime().getDayOfMonth() + "-" +
                transaction.getLocalTime().getMonthValue() + "-" + transaction.getLocalTime().getYear() + ", ");
        System.out.println(transaction.getLocalTime().getHour() + ":" + transaction.getLocalTime().getMinute());
        System.out.println("Amount: \t \t \t" + transaction.getAmount());
        System.out.println("Type: \t \t \t" + transaction.getType().getTypeDescription());
        System.out.println("Description: \t \t" + transaction.getDescription());
        System.out.println("ID: \t \t \t" + transaction.getId());
        System.out.println("-------------------------------------------");
        System.out.println();
    }
}

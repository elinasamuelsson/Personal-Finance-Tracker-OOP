package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.*;
import examinationsprojekt.repositories.AccountDatabaseRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.util.InputMismatchException;
import java.util.List;

public class AddAccountCommand implements ICommand {
    private final int index = 1;
    private final String description = "Add account";

    public AddAccountCommand() {
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IAccountRepository repository;
                try {
                repository = new AccountDatabaseRepository(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASS")
        );
                } catch (Exception e) {
                    throw new RuntimeException("Could not connect to database.", e);
                }

        Account account = null;

        AccountTypes type = returnAccountType();
        String name = returnAccountName();

        if (type.equals(AccountTypes.CHECKING)) {
            account = new CheckingAccount(name, type);
        } else if (type.equals(AccountTypes.SAVING)) {
            double interest = returnAccountInterest();

            account = new SavingsAccount(name, type, interest);
        }

        List<Account> userAccounts;
                try {
                    userAccounts = repository.findAllUserAccounts();
                } catch (Exception e) {
                    throw new RuntimeException("Could not connect to database.", e);
                }

        if (!userAccounts.isEmpty()) {
            for (Account existingAccount : userAccounts) {
                if (existingAccount.getName().equals(account.getName())) {
                    System.out.println("Account name already exists in another account.");
                    System.out.println("Restart account creation and try again.");
                    return;
                }
            }
        }

        try {
            repository.save(account, CurrentStateManager.getCurrentUser().getId());
            System.out.println("Account successfully created.");
            System.out.println("Returning to menu.");
        } catch (Exception e) {
            throw new RuntimeException("Could not save account.\nReturning to menu.", e);
        }
    }


    private AccountTypes returnAccountType() {
        System.out.println("What type of account do you wish to create?");
        for (int i = 0; i < AccountTypes.values().length; i++) {
            System.out.println("\t" + (i + 1) + ". " + AccountTypes.values()[i].getTypeDescription());
        }

        int userInput = 0;
        while (true) {
            userInput = input.intInput();
            try {
                if (userInput <= 0 || userInput > AccountTypes.values().length) {
                    System.out.println("Please enter a valid option.");
                } else {
                    return AccountTypes.values()[(userInput - 1)];
                }
            } catch (InputMismatchException exception) {
                System.out.println("Option does not exist. Please enter a valid option.");
            }
        }
    }

    private String returnAccountName() {
        System.out.println("Enter the name you wish to give this account.");

        String userInput = "";
        while (true) {
            userInput = input.stringInput();

            if (userInput.isEmpty()) {
                System.out.println("Account must have a name.");
            } else break;
        }
        return userInput;
    }

    private double returnAccountInterest() {
        System.out.println("Enter your savings account's monthly interest.");
        System.out.println("Please use decimal format, where for example 0.03 represents 3%.");

        double MAX_INTEREST_RATE = 0.99;
        double MIN_INTEREST_RATE = 0.001;
        double userInput = 0;
        while (true) {
            try {
                userInput = input.doubleInput();
                if (userInput < MIN_INTEREST_RATE) {
                    System.out.println("Interest cannot be 0 or lower.");
                    continue;
                } else if (userInput > MAX_INTEREST_RATE) {
                    System.out.println("Interest cannot be greater than 99%.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Interest must be a valid number.");
            }
        }
        return userInput;
    }
}

package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.models.TransactionTypes;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.*;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AddTransactionCommand implements ICommand {
    private final int index = 4;
    private final String description = "Add transaction";

    public AddTransactionCommand() {
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        if (CurrentStateManager.getCurrentAccount() == null) {
            System.out.println("Select an account before adding transactions.");
            return;
        }

        IAccountRepository accountRepository;
        ITransactionRepository transactionRepository;

        try {
            accountRepository = new AccountDatabaseRepository(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS")
            );

            transactionRepository = new TransactionDatabaseRepository(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS")
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to database.", e);
        }

        boolean isEarning = returnIsEarning();
        TransactionTypes type = returnType();
        Instant time = returnTime();
        String description = returnDescription();
        double amount = 0;
        if (isEarning) {
            amount = returnAmount();
        } else {
            amount = Double.parseDouble(("-" + returnAmount()));
        }

        Transaction transaction = new Transaction(amount, time, type, description, isEarning);

        try {
            transactionRepository.save(transaction);
            System.out.println("Transaction has been saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            UUID accountId = CurrentStateManager.getCurrentAccount().getId();
            accountRepository.updateAccountBalance(accountId, amount);
            System.out.println("Account balance has been updated successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Could not update account balance.", e);
        }
    }

    private TransactionTypes returnType() {
        System.out.println("What type of transaction do you wish to add?");

        for (int i = 0; i < TransactionTypes.values().length; i++) {
            System.out.println("\t" + (i + 1) + ". " + TransactionTypes.values()[i]);
        }

        int userInput = 0;
        while (true) {
            userInput = input.intInput();
            try {
                if (userInput == 0 ||
                        userInput >= TransactionTypes.values().length) {
                    System.out.println("Please enter a valid option.");
                } else {
                    return TransactionTypes.values()[(userInput - 1)];
                }
            } catch (InputMismatchException exception) {
                System.out.println(exception.getMessage());
                System.out.println("Please enter a valid number option.");
            }
        }
    }

    private Instant returnTime() {
        System.out.println("Enter date and time of the transaction.");
        System.out.println("Please use the format YYYY-MM-DD HH:MM");

        LocalDateTime timeLDT;
        Instant time;
        String userInput = "";
        while (true) {
            try {
                userInput = input.stringInput();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                timeLDT = LocalDateTime.parse(userInput, formatter);

                time = timeLDT.toInstant(ZoneOffset.UTC);
                return time;
            } catch (DateTimeParseException exception) {
                System.out.println("Invalid date format. Try again.");
                System.out.println("Please use the format YYYY-MM-DD HH:MM");
            }
        }
    }

    private String returnDescription() {
        System.out.println("Briefly describe the transaction.");

        String userInput = "";
        while (true) {
            userInput = input.stringInput();

            if (userInput.isEmpty()) {
                System.out.println("Transaction must have a description.");
            } else {
                return userInput;
            }
        }
    }

    private boolean returnIsEarning() {
        System.out.println("Did you earn or spend money?");
        System.out.println("1. Earn");
        System.out.println("2. Spend");

        String userInput = "";
        while (true) {
            userInput = input.stringInput();

            if (userInput.equals("1")) {
                return true;
            } else if (userInput.equals("2")) {
                return false;
            } else {
                System.out.println("Enter a valid number response.");
            }
        }
    }

    private double returnAmount() {
        System.out.println("Enter transaction amount.");

        double MAX_AMOUNT = 200000;

        double userInput = 0;
        while (true) {
            try {
                userInput = input.doubleInput();

                if (userInput >= MAX_AMOUNT) {
                    System.out.println("Amount cannot be greater than 200000.");
                } else if (userInput <= 0) {
                    System.out.println("Amount must be greater than 0.");
                } else {
                    return userInput;
                }
            } catch (InputMismatchException | NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}

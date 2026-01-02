package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.AccountDatabaseRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.util.List;

public class SelectAccountCommand implements ICommand {
    private final int index = 2;
    private final String description = "Select account";

    public SelectAccountCommand() {
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

        System.out.println("Which of the following accounts do you want to use?");
        System.out.println();

        List<Account> accounts;

        try {
        accounts = repository.findAllUserAccounts();
        } catch (Exception e) {
            throw new RuntimeException("Could not find accounts in the database.", e);
        }

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : accounts) {
            System.out.print((accounts.indexOf(account) + 1) + ":");
            System.out.println("\t" + account.getName() + ", " + account.getType().getTypeDescription());
            System.out.println();
        }

        int userInput = input.intInput();

        for (Account account : accounts) {
            if (userInput != (accounts.indexOf(account) + 1)) {
                continue;
            }

            if (userInput == (accounts.indexOf(account) + 1)) {
                CurrentStateManager.setCurrentAccount(account);
                System.out.println();
                System.out.println(account.getName() + " selected.");
                return;
            } else {
                System.out.println();
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}

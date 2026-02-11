package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.repositories.AccountDatabaseRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteAccountCommand implements ICommand {
    private final int index = 3;
    private final String description = "Delete account";

    public DeleteAccountCommand() {
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

        System.out.println("Which of the following accounts do you want to delete?");
        System.out.println();

        Account accountToRemove = null;
        Optional<List<Account>> optUserAccounts;

        try {
            optUserAccounts = repository.findAllUserAccounts();
        } catch (Exception e) {
            throw new RuntimeException("Could not find accounts in the database.", e);
        }

        List<Account> userAccounts = optUserAccounts.orElse(new ArrayList<>());

        if (userAccounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : userAccounts) {
            System.out.print((userAccounts.indexOf(account) + 1) + ":");
            System.out.println("\t" + account.getName() + ", " + account.getType().getTypeDescription());
            System.out.println();
        }

        int userInput = input.intInput();

        for (Account account : userAccounts) {
            if (userInput == (userAccounts.indexOf(account) + 1)) {
                if (CurrentStateManager.getCurrentAccount() != null) {
                    if (CurrentStateManager.getCurrentAccount().getName().equals(account.getName())) {
                        CurrentStateManager.setCurrentAccount(null);
                        System.out.println(account.getName() + " was unselected because of impending deletion.");
                    }
                }

                accountToRemove = account;
            }
        }

        try {
            repository.delete(accountToRemove);
            System.out.println(accountToRemove.getName() + " deleted.");
        } catch (Exception e) {
            System.out.println(accountToRemove.getName() + " could not be deleted.");
        }
    }
}

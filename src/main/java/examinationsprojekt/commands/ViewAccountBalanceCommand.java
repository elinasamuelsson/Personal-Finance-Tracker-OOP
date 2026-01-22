package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.repositories.AccountDatabaseRepository;
import examinationsprojekt.repositories.IAccountRepository;

public class ViewAccountBalanceCommand implements ICommand {
    private final int index = 8;
    private final String description = "View account balance";

    public ViewAccountBalanceCommand() {
    }

    public void run() {
        IAccountRepository repository;

        try {
            repository = new AccountDatabaseRepository(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS")
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to database,", e);
        }

        if (CurrentStateManager.getCurrentAccount() == null) {
            System.out.println("Select an account before viewing account balance.");
            return;
        }

        Account account;

        try {
            account = repository.findSingleAccount(
                    CurrentStateManager.getCurrentAccount().getName()
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to database.", e);
        }

        System.out.println("Your current account balance is " + account.getBalance());
    }
}

package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.repositories.IUserRepository;

import java.io.IOException;
import java.util.List;

public class ViewAccountBalanceCommand implements ICommand {
    private final int index = 8;
    private final String description = "View account balance";

    public ViewAccountBalanceCommand() {
    }

    public void run() {
        IUserRepository repository = new UserFileRepository();

        User user = null;
        Account accountToPrintBalanceFrom = null;

        try {
            user = repository.findSingleUser(
                    CurrentStateManager.getCurrentUser().getUsername()
            );
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        if (CurrentStateManager.getCurrentAccount() == null) {
            System.out.println("Select an account before viewing account balance.");
            return;
        }

        List<Account> accounts = user.getAccounts();

        for (Account a : accounts) {
            if (a.getName().equals(CurrentStateManager.getCurrentAccount().getName())) {
                accountToPrintBalanceFrom = a;
            }
        }


        if (accountToPrintBalanceFrom == null) {
            System.out.println("Select an account before viewing account balance.");
            return;
        }

        System.out.println("Your current account balance is " + accountToPrintBalanceFrom.getBalance());
    }
}

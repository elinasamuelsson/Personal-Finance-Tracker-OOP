package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.util.List;

public class DeleteAccountCommand implements ICommand {
    private final int index = 3;
    private final String description = "Delete account";

    public DeleteAccountCommand() {
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IUserRepository repository = new UserFileRepository();

        System.out.println("Which of the following accounts do you want to delete?");
        System.out.println();

        User user = null;
        Account accountToRemove = null;

        try {
            user = repository.findSingleUser(
                    CurrentStateManager.getCurrentUser().getUsername()
            );
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No such user.");
        }

        List<Account> allUserAccounts = user.getAccounts();

        if (allUserAccounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : allUserAccounts) {
            System.out.print((allUserAccounts.indexOf(account) + 1) + ":");
            System.out.println("\t" + account.getName() + ", " + account.getType().getTypeDescription());
            System.out.println();
        }

        int userInput = input.intInput();

        for (Account account : allUserAccounts) {
            if (userInput == (allUserAccounts.indexOf(account) + 1)) {
                if (CurrentStateManager.getCurrentAccount() != null) {
                    if (CurrentStateManager.getCurrentAccount().getName().equals(account.getName())) {
                        CurrentStateManager.setCurrentAccount(null);
                        System.out.println(account.getName() + " was unselected because of impending deletion.");
                    }
                }

                accountToRemove = account;
            }
        }
        user.removeAccountFromList(accountToRemove);

        try {
            repository.update(user);
            System.out.println(accountToRemove.getName() + " deleted.");
        } catch (IOException e) {
            System.out.println(accountToRemove.getName() + " could not be deleted.");
        }
    }
}

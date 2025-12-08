package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.repositories.AccountFileRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.util.List;

public class DeleteAccountCommand implements ICommand {
    private final int index = 3;
    private final String description = "Delete account";

    public DeleteAccountCommand() {}

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IAccountRepository repository = new AccountFileRepository();

        System.out.println("Which of the following accounts do you want to delete?");
        System.out.println();

        List<Account> accounts = repository.findAll();

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : accounts) {
            System.out.print((accounts.indexOf(account) + 1) + ":");
            System.out.println("\t" + account.getName() + ", " + account.getType().getTypeDescription());
            System.out.println("\t" + account.getOwner());
            System.out.println();
        }

        int userInput = input.intInput();

        for (Account account : accounts) {
            if (userInput == (accounts.indexOf(account) + 1)) {
                if (CurrentStateManager.getCurrentAccount() != null) {
                    if (CurrentStateManager.getCurrentAccount().getName().equals(account.getName())) {
                        CurrentStateManager.setCurrentAccount(null);
                        System.out.println(account.getName() + " was unselected because of impending deletion.");
                    }
                }

                repository.delete(account);
                System.out.println(account.getName() + " deleted.");
            }
        }
    }
}

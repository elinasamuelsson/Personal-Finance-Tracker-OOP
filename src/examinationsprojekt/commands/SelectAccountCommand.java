package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.repositories.AccountFileRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.util.List;

public class SelectAccountCommand implements ICommand {
    private final int index = 2;
    private final String description = "Select account";

    public SelectAccountCommand() {
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IAccountRepository repository = new AccountFileRepository();

        System.out.println("Which of the following accounts do you want to use?");
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

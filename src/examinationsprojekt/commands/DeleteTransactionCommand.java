package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;

public class DeleteTransactionCommand implements ICommand {
    private final int index = 6;
    private final String description = "Delete transaction";

    public DeleteTransactionCommand() {
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IUserRepository repository = new UserFileRepository();
        User userToDeleteFrom = null;
        Account accountToDeleteFrom = null;

        try {
            userToDeleteFrom = repository.findSingleUser(
                    CurrentStateManager.getCurrentUser().getUsername()
            );
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (CurrentStateManager.getCurrentAccount() == null) {
            System.out.println("Select an account before deleting transactions.");
            return;
        } else {
            for (Account account : userToDeleteFrom.getAccounts()) {
                if (account.getName().equals(CurrentStateManager.getCurrentAccount().getName())) {
                    accountToDeleteFrom = account;
                }
            }
        }


        String userInput = "";
        while (true) {
            System.out.println("Enter the ID of the transaction you wish to delete.");
            userInput = input.stringInput();

            boolean transactionFound = false;

            for (Transaction transaction : accountToDeleteFrom.getTransactionsCopy()) {
                if (transaction.getId().equals(userInput)) {
                    accountToDeleteFrom.removeTransactionFromList(transaction);
                    transactionFound = true;
                }
            }

            if (!transactionFound) {
                System.out.println("No such transaction found.");
                System.out.println("Restart transaction deletion and try again.");
                return;
            }

            try {
                repository.update(userToDeleteFrom);
                System.out.println("Transaction has been deleted.");
                break;
            } catch (IOException e) {
                System.out.println("Transaction could not be deleted.");
            }
        }
    }
}

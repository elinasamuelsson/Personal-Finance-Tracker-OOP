package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.ITransactionRepository;
import examinationsprojekt.repositories.TransactionDatabaseRepository;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.util.UUID;

public class DeleteTransactionCommand implements ICommand {
    private final int index = 6;
    private final String description = "Delete transaction";

    public DeleteTransactionCommand() {
    }

    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        ITransactionRepository repository;

        try {
            repository = new TransactionDatabaseRepository(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS")
            );
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to database.", e);
        }

        String userInput = "";
        while (true) {
            System.out.println("Enter the ID of the transaction you wish to delete.");
            userInput = input.stringInput();
            UUID id = UUID.fromString(userInput);

            try {
            if(repository.delete(id)) {
                System.out.println("Transaction has been deleted.");
                break; }
            } catch (Exception e) {
                System.out.println("Transaction could not be deleted.");
            }
        }
    }
}

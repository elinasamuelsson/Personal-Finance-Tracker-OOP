package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.repositories.ITransactionRepository;
import examinationsprojekt.repositories.TransactionDatabaseRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchTransactionCommand implements ICommand {
    private final int index = 7;
    private final String description = "Search transactions";

    public SearchTransactionCommand() {
    }

    IUserInputReader input = new UserTerminalInputReader();

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

        if (CurrentStateManager.getCurrentAccount() == null) {
            System.out.println("Select an account before searching transactions.");
            return;
        }

        Optional<List<Transaction>> optSearchResults;
        String userInput = "";
        while (true) {
            System.out.println("Enter the phrase you wish to search for.");
            userInput = input.stringInput().toLowerCase();

            try {
                optSearchResults = repository.searchTransactions(userInput);
            } catch (Exception e) {
                throw new RuntimeException("Could not connect to database.", e);
            }

            List<Transaction> searchResults = optSearchResults.orElse(new ArrayList<>());

            printSearchResults(searchResults);
            searchResults.clear();
            return;
        }
    }

    private void printSearchResults(List<Transaction> searchResults) {
        if (searchResults.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : searchResults) {
                System.out.println("-------------------------------------------");
                System.out.print("Time: \t \t \t" + transaction.getLocalTime().getDayOfMonth() + "-" +
                        transaction.getLocalTime().getMonthValue() + "-" + transaction.getLocalTime().getYear() + ", ");
                System.out.println(transaction.getLocalTime().getHour() + ":" + transaction.getLocalTime().getMinute());
                System.out.println("Amount: \t \t \t" + transaction.getAmount());
                System.out.println("Type: \t \t \t" + transaction.getType().getTypeDescription());
                System.out.println("Description: \t \t" + transaction.getDescription());
                System.out.println("ID: \t \t \t" + transaction.getId());
                System.out.println("-------------------------------------------");
            }
        }
    }
}

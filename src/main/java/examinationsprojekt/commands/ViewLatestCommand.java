package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.repositories.ITransactionRepository;
import examinationsprojekt.repositories.TransactionDatabaseRepository;

import java.util.HashMap;
import java.util.Map;

public class ViewLatestCommand implements ICommand {
    private final int index = 9;
    private final String description = "View latest transactions";

    public ViewLatestCommand() {
    }

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

        HashMap<String, Transaction> latestTransactions;
        try {
            latestTransactions = repository.findLatestTransactionForEachUserAccount(CurrentStateManager.getCurrentUser().getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not find latest transactions.", e);
        }

        for (Map.Entry<String, Transaction> entry : latestTransactions.entrySet()) {
            String accountName = entry.getKey();
            Transaction transaction = entry.getValue();

            System.out.println("-------------------------------------------");
            System.out.println("Account: \t \t \t" + accountName);
            System.out.print("Time: \t \t \t" +
                    transaction.getLocalTime().getDayOfMonth() + "-" +
                    transaction.getLocalTime().getMonthValue() + "-" +
                    transaction.getLocalTime().getYear() + ", ");
            System.out.println(
                    transaction.getLocalTime().getHour() + ":" +
                            transaction.getLocalTime().getMinute()
            );
            System.out.println("Amount: \t \t \t" + transaction.getAmount());
            System.out.println("Type: \t \t \t" + transaction.getType().getTypeDescription());
            System.out.println("Description: \t \t" + transaction.getDescription());
            System.out.println("ID: \t \t \t" + transaction.getId());
            System.out.println("-------------------------------------------");
            System.out.println();
        }

    }
}

package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Account;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.repositories.AccountFileRepository;
import examinationsprojekt.repositories.IAccountRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.util.ArrayList;
import java.util.List;

public class SearchTransactionCommand implements ICommand {
    private final int index = 7;
    private final String description = "Search transactions";

    public SearchTransactionCommand() {}

    IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IAccountRepository repository = new AccountFileRepository();
        Account accountToSearchFrom = null;

        if (CurrentStateManager.getCurrentAccount() == null) {
            System.out.println("Select an account before searching transactions.");
            return;
        } else {
            List<Account> accounts = repository.findAll();
            for (Account account : accounts) {
                if (account.getName().equals(CurrentStateManager.getCurrentAccount().getName())) {
                    accountToSearchFrom = account;
                }
            }
        }

        List<Transaction> searchResults = new ArrayList<>();
        String userInput = "";
        while (true) {
            System.out.println("Enter the phrase you wish to search for.");
            userInput = input.stringInput().toLowerCase();

            for (Transaction transaction : accountToSearchFrom.getTransactionsCopy()) {
                if (transaction.getId().toLowerCase().contains(userInput) ||
                transaction.getDescription().toLowerCase().contains(userInput) ||
                transaction.getType().toString().toLowerCase().contains(userInput) ||
                transaction.getLocalTime().toString().toLowerCase().contains(userInput)) {
                    searchResults.add(transaction);
                }
            }
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

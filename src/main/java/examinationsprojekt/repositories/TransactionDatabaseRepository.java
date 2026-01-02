package examinationsprojekt.repositories;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.models.Transaction;
import examinationsprojekt.models.TransactionTypes;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDatabaseRepository implements ITransactionRepository {
    Connection connection;

    public TransactionDatabaseRepository(String url, String username, String password) throws Exception {
        connection = DriverManager.getConnection(url, username, password);

        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS transactions (" +
                            "id UUID PRIMARY KEY," +
                            "account_id UUID references accounts(id) NOT NULL," +
                            "amount DECIMAL NOT NULL," +
                            "time TIMESTAMPTZ," +
                            "transaction_type TEXT NOT NULL," +
                            "description TEXT NOT NULL," +
                            "isEarning BOOLEAN NOT NULL)"
            );
        }
    }

    public void save(Transaction createdTransaction) throws SQLException {
        String sql =  "INSERT INTO transactions (id, account_id, amount, time, transaction_type, description, isearning) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, createdTransaction.getId());
            statement.setObject(2, CurrentStateManager.getCurrentAccount().getId());
            statement.setDouble(3, createdTransaction.getAmount());
            statement.setTimestamp(4, Timestamp.from(createdTransaction.getUTCTime()));
            statement.setString(5, createdTransaction.getType().toString());
            statement.setString(6, createdTransaction.getDescription());
            statement.setBoolean(7, createdTransaction.isEarning());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Failed to save transaction.");
            }
        }
    }

    public Transaction findSingleTransaction(UUID transactionId) throws Exception {
        return null;
    }

    public List<Transaction> findAllAccountTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE account_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, CurrentStateManager.getCurrentAccount().getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("time").toInstant(),
                        TransactionTypes.valueOf(resultSet.getString("transaction_type")),
                        resultSet.getString("description"),
                        resultSet.getBoolean("isEarning")
                );
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public List<Transaction> searchTransactions(String searchPhrase) throws Exception {
        List<Transaction> searchResults = new ArrayList<>();

        String sql = "SELECT * FROM transactions " +
                "WHERE id::text ILIKE ? OR description ILIKE ? OR transaction_type ILIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchpattern = "%" + searchPhrase + "%";

            statement.setString(1, searchpattern);
            statement.setString(2, searchpattern);
            statement.setString(3, searchpattern);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getDouble("amount"),
                        resultSet.getTimestamp("time").toInstant(),
                        TransactionTypes.valueOf(resultSet.getString("transaction_type")),
                        resultSet.getString("description"),
                        resultSet.getBoolean("isEarning")
                );
                searchResults.add(transaction);
            }
        }
        return searchResults;
    }

    public boolean delete(UUID id) throws Exception {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            return statement.executeUpdate() == 1;
        }
    }
}

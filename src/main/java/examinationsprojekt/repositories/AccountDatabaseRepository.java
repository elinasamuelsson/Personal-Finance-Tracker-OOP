package examinationsprojekt.repositories;

import examinationsprojekt.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDatabaseRepository implements IAccountRepository {
    Connection connection;

    public AccountDatabaseRepository(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, "postgres", password);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "id UUID PRIMARY KEY," +
                    "user_id UUID references users(id) NOT NULL," +
                    "account_name TEXT NOT NULL," +
                    "account_type TEXT NOT NULL," +
                    "balance DECIMAL NOT NULL," +
                    "interest_rate DECIMAL)");
        }
    }

    public void save(Account account, UUID userId) throws Exception {
        String sql = "INSERT INTO accounts (id, user_id, account_name, account_type, balance, interest_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, account.getId());
            statement.setObject(2, userId);
            statement.setString(3, account.getName());
            statement.setString(4, account.getType().toString());
            statement.setDouble(5, account.getBalance());
            statement.setDouble(6, account.getMonthlyInterestRate());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Failed to save account.");
            }
        }
    }


    public Account findSingleAccount(String accountName) throws Exception {
        Account account = null;

        String sql = "SELECT * FROM accounts WHERE account_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountName);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }
            String type = resultSet.getString("account_type");

            if (type.equals(AccountTypes.CHECKING.toString())) {
                account = new CheckingAccount(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getString("account_name"),
                        AccountTypes.valueOf(type),
                        resultSet.getDouble("balance")
                ) {
                };
            } else if (type.equals(AccountTypes.SAVING.toString())) {
                account = new SavingsAccount(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getString("account_name"),
                        AccountTypes.valueOf(type),
                        resultSet.getDouble("balance"),
                        resultSet.getDouble("interest_rate")
                );
            }
        }
        return account;
    }

    public List<Account> findAllUserAccounts() throws Exception {
            List<Account> accounts = new ArrayList<>();

            String sql = "SELECT * FROM accounts";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String type = resultSet.getString("account_type");
                if (type.equals(AccountTypes.CHECKING.toString())) {
                    Account account = new CheckingAccount(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("account_name"),
                            AccountTypes.valueOf(type),
                            resultSet.getDouble("balance")
                    );
                    accounts.add(account);
                } else if (type.equals(AccountTypes.SAVING.toString())) {
                    Account account = new SavingsAccount(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("account_name"),
                            AccountTypes.valueOf(type),
                            resultSet.getDouble("balance"),
                            resultSet.getDouble("interest_rate")
                    );
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

    public boolean updateAccountBalance(UUID id, double amount) throws Exception {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            statement.setObject(2, id);

            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(Account account) throws SQLException {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, account.getId());

            return statement.executeUpdate() == 1;
        }
    }
}

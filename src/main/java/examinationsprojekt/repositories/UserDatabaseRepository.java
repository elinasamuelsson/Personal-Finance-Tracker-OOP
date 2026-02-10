package examinationsprojekt.repositories;

import examinationsprojekt.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDatabaseRepository implements IUserRepository {
    // TODO: switch to returning Optional<User> where applicable if there is time
    private Connection connection;

    public UserDatabaseRepository(String url, String user, String password) throws Exception {
        connection = DriverManager.getConnection(url, user, password);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id UUID PRIMARY KEY," +
                    "username TEXT," +
                    "password_hash TEXT)");
        }
    }

    public void save(User createdUser) throws Exception {
        String sql = "INSERT INTO users (id, username, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, createdUser.getId());
            statement.setString(2, createdUser.getUsername());
            statement.setString(3, createdUser.getPasswordHash());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Failed to save user to database.");
            }
        }
    }

    public Optional<User> findSingleUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(
                    resultSet.getObject("id", UUID.class),
                    resultSet.getString("username"),
                    resultSet.getString("password_hash")
            ));
        }
    }

    public Optional<List<User>> findAllUsers() throws Exception {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash")
                );
                users.add(user);
            }
        }

        return Optional.of(users);
    }
}

package examinationsprojekt.repositories;

import examinationsprojekt.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    void save(User createdUser) throws Exception;

    Optional<User> findSingleUser(String username) throws SQLException;

    Optional<List<User>> findAllUsers() throws Exception;
}

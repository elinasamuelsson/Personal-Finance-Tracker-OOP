package examinationsprojekt.repositories;

import examinationsprojekt.models.User;

import java.io.IOException;
import java.util.List;

public class UserDatabaseRepository {
    void save(User createdUser) throws IOException {

    };

    User findSingleUser(String username) throws IOException, ClassNotFoundException {
        return null;
    };

    List<User> findAll() throws IOException, ClassNotFoundException {
        return null;
    };

    boolean update(User updatedUser) throws IOException {
        return false;
    };

    void delete(User deletedUser) throws IOException {};
}

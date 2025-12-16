package examinationsprojekt.repositories;

import examinationsprojekt.models.User;

import java.io.IOException;
import java.util.List;

public interface IUserRepository {
    void save(User createdUser) throws IOException;

    User findSingleUser(String username) throws IOException, ClassNotFoundException;

    List<User> findAll() throws IOException, ClassNotFoundException;

    boolean update(User updatedUser) throws IOException;

    void delete(User deletedUser) throws IOException;
}

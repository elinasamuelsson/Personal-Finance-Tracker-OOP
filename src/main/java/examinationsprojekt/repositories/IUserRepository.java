package examinationsprojekt.repositories;

import examinationsprojekt.models.User;

import java.util.List;

public interface IUserRepository {
    void save(User createdUser) throws Exception;

    User findSingleUser(String username) throws Exception;

    List<User> findAllUsers() throws Exception;

    boolean update(User updatedUser) throws Exception;

    void delete(User deletedUser) throws Exception;
}

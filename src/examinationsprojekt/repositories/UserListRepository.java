package examinationsprojekt.repositories;

import examinationsprojekt.models.*;

import java.util.ArrayList;
import java.util.List;

public class UserListRepository implements IUserRepository {
    private static final List<User> users = new ArrayList<>();

    public void save(User createdUser) {
        users.add(createdUser);
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public boolean update(User updatedUser) {
        for (User user : users) {
            if (user.getUsername().equals(updatedUser.getUsername())) {
                users.set(users.indexOf(user), updatedUser);
                return true;
            }
        }
        return false;
    }

    public void delete(User deletedUser) {
        users.remove(deletedUser);
    }
}

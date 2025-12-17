package examinationsprojekt.repositories;

import examinationsprojekt.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class UserFileRepository implements IUserRepository {
    public void save(User createdUser) throws IOException {
        File folder = new File("saveData");
        if (!folder.exists()) {
            folder.mkdir();
        }
        try (ObjectOutputStream objectOut = new ObjectOutputStream(
                new FileOutputStream(
                        new File(folder, createdUser.getUsername())
                )
        )) {
            objectOut.writeObject(createdUser);
        }

    }

    public User findSingleUser(String username) throws IOException, ClassNotFoundException {
        User user;
        File folder = new File("saveData");

        if (!folder.exists() || folder.listFiles() == null) {
            return null;
        }

        for (File file : folder.listFiles()) {
            if (file.getName().equals(username)) {
                try (ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(file))) {
                    user = (User) objectIn.readObject();
                }
                return user;
            }
        }
        return null;
    }

    public List<User> findAllUsers() throws IOException, ClassNotFoundException {
        List<User> users = new ArrayList<>();
        File folder = new File("saveData");

        if (!folder.exists() || folder.listFiles() == null) {
            return users;
        }
        for (File file : folder.listFiles()) {
            try (ObjectInputStream objectIn = new ObjectInputStream(
                    new FileInputStream(file))
            ) {
                users.add((User) objectIn.readObject());
            }
        }
        return users;
    }

    public boolean update(User updatedUser) throws IOException {

        File folder = new File("saveData");
        for (File file : folder.listFiles()) {
            if (file.getName().equals(updatedUser.getUsername())) {
                try (ObjectOutputStream objectOut = new ObjectOutputStream(
                        new FileOutputStream(
                                new File(folder, updatedUser.getUsername())
                        )
                )) {
                    objectOut.writeObject(updatedUser);
                }
            }
        }

        return true;
    }

    public void delete(User deletedUser) {
        try {
            File folder = new File("saveData");
            for (File file : folder.listFiles()) {
                if (file.getName().equals(deletedUser.getUsername())) {
                    file.delete();
                    return;
                }
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }

    }
}

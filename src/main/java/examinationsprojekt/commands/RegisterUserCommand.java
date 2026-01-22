package examinationsprojekt.commands;

import examinationsprojekt.models.User;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.repositories.UserDatabaseRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserCommand {
    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IUserRepository repository;

        try {
            repository = new UserDatabaseRepository(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<User> users;
        List<String> existingUsernames = new ArrayList<>();

        try {
            users = repository.findAllUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (User user : users) {
            existingUsernames.add(user.getUsername());
        }

        System.out.println("Enter the username you wish to use.");
        String username = "";

        while (true) {
            username = returnTextString();

            if (existingUsernames.contains(username)) {
                System.out.println("Invalid username, try another one.");
            } else break;
        }

        System.out.println("Enter the password you wish to use.");
        String password = "";
        while (true) {
            password = returnTextString();

            if (!password.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*")) {
                System.out.println("Password must contain at least one special character.");
            } else break;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));

        User user = new User(username, passwordHash);

        try {
            repository.save(user);
            System.out.println("User profile successfully created.");
        } catch (Exception e) {
            throw new RuntimeException("User creation failed.");
        }
        System.out.println("Returning to menu.");
    }

    private String returnTextString() {
        String userInput = "";
        while (true) {
            userInput = input.stringInput();

            if (userInput.isEmpty()) {
                System.out.println("Text field cannot be empty.");
            } else break;
        }
        return userInput;
    }
}

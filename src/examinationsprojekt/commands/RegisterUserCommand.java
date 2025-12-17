package examinationsprojekt.commands;

import examinationsprojekt.models.User;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterUserCommand {
    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IUserRepository repository = new UserFileRepository();

        List<User> users;
        List<String> existingUsernames = new ArrayList<>();

        try {
            users = repository.findAllUsers();
        } catch (IOException | ClassNotFoundException e) {
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
                System.out.println("Username is already in use.");
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

        User user = new User(username, password);

        try {
            repository.save(user);
            System.out.println("User profile successfully created.");
        } catch (IOException e) {
            System.out.println("User creation failed.");
            throw new RuntimeException(e);
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

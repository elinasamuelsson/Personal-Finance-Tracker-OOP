package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.managers.ICommandManager;
import examinationsprojekt.managers.TerminalCommandManager;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.repositories.UserFileRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

import java.io.IOException;
import java.util.Objects;

public class LoginUserCommand {
    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IUserRepository repository = new UserFileRepository();

        System.out.println("Enter your username and password: ");

        User user = null;
        String usernameInput;
        String passwordInput;
        while (true) {
            System.out.print("Username: ");
            usernameInput = input.stringInput();

            System.out.print("Password: ");
            passwordInput = input.stringInput();

            //hash password input and compare hashed input to hashed stored password

            try {
                user = repository.findSingleUser(usernameInput);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Invalid username or password.");
            }

            if (user == null || !Objects.equals(passwordInput, user.getPassword())) {
                System.out.println("Invalid username or password.");
            } else if (passwordInput.equals(user.getPassword())) {
                System.out.println("Login successful.");
                CurrentStateManager.setCurrentUser(user);
                break;
            }
        }

        ICommandManager commandManager = new TerminalCommandManager();
        commandManager.run();
    }
}

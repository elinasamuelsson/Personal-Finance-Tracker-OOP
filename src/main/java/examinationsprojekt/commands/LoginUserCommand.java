package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.managers.ICommandManager;
import examinationsprojekt.managers.TerminalCommandManager;
import examinationsprojekt.models.User;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.repositories.UserDatabaseRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Optional;

public class LoginUserCommand {
    private final IUserInputReader input = new UserTerminalInputReader();

    public void run() {
        IUserRepository repository;


        try {
            repository = new UserDatabaseRepository(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASS"));
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to database.", e);
        }

        System.out.println("Enter your username and password: ");

        while (true) {
            Optional<User> optUser = Optional.empty();
            String usernameInput;
            String passwordInput;

            System.out.print("Username: ");
            usernameInput = input.stringInput();

            System.out.print("Password: ");
            passwordInput = input.stringInput();

            try {
                optUser = repository.findSingleUser(usernameInput);
            } catch (SQLException e) {
                System.out.println("No such user or password.");
                continue;
            }

            if (optUser.isEmpty()) {
                System.out.println("No such user or password.");
                continue;
            }

            User user = optUser.get();

            if (!BCrypt.checkpw(passwordInput, user.getPasswordHash())) {
                System.out.println("No such user or password.");
                continue;
            }

            System.out.println("Login successful.");
            System.out.println();
            CurrentStateManager.setCurrentUser(user);
            break;
        }

        ICommandManager commandManager = new TerminalCommandManager();
        commandManager.run();
    }
}

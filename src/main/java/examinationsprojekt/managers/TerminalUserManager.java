package examinationsprojekt.managers;

import examinationsprojekt.commands.LoginUserCommand;
import examinationsprojekt.commands.RegisterUserCommand;
import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.repositories.UserDatabaseRepository;
import examinationsprojekt.utils.IUserInputReader;
import examinationsprojekt.utils.UserTerminalInputReader;

public class TerminalUserManager implements ICommandManager {
    private final IUserInputReader input = new UserTerminalInputReader();
    public void run() {
        System.out.println("Welcome to the revised OOP version of the Personal Finance Tracker!");
        System.out.println("Now with database connection for more effective data storage.");

        RegisterUserCommand registerUserCommand = new RegisterUserCommand();
        LoginUserCommand loginUserCommand = new LoginUserCommand();

        while (true) {
            printMenuOptions();

            String userInput = input.stringInput();

            if (userInput.equals("1")) {
                loginUserCommand.run();
            } else if (userInput.equals("2")) {
                registerUserCommand.run();
            } else if (userInput.equals("0")) {
                System.out.println("Exiting program.");
                System.exit(0);
            }
        }
    }

    private void printMenuOptions() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Please select an option from the following:");

        System.out.println("\t1. Login");
        System.out.println("\t2. Register user");
        System.out.println("\t0. Exit program");
        System.out.println("-------------------------------------------");
        System.out.println();
    }
}

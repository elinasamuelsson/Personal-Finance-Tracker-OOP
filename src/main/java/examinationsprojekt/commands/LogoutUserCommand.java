package examinationsprojekt.commands;

import examinationsprojekt.managers.CurrentStateManager;
import examinationsprojekt.managers.TerminalUserManager;

public class LogoutUserCommand implements ICommand {
    private final int index = 10;
    private final String description = "Logout user";

    TerminalUserManager userManager = new TerminalUserManager();

    public LogoutUserCommand() {}

    public void run() {
        CurrentStateManager.setCurrentUser(null);
        CurrentStateManager.setCurrentAccount(null);

        System.out.println("You have been successfully logged out.");
        System.out.println();
        userManager.run();
    }
}

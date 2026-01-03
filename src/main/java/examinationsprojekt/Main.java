package examinationsprojekt;

import examinationsprojekt.managers.TerminalUserManager;

public class Main {
    public static void main(String[] args) {
        TerminalUserManager terminalUserManager = new TerminalUserManager();
        terminalUserManager.run();

        // TODO: don't read CurrentStateManager within the repository, send as argument
    }
}

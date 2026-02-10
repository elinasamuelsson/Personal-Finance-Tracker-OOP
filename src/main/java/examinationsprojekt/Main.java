package examinationsprojekt;

import examinationsprojekt.managers.TerminalUserManager;

public class Main {
    public static void main(String[] args) {
        TerminalUserManager terminalUserManager = new TerminalUserManager();
        terminalUserManager.run();
    }

    //TODO: TransactionDatabaseRepository, AccountDatabaseRepository needs Optional returns
}

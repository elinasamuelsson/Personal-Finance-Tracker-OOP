package examinationsprojekt.managers;

import examinationsprojekt.models.Account;

public class CurrentStateManager {
    private static Account currentAccount;

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void setCurrentAccount(Account currentAccount) {
        CurrentStateManager.currentAccount = currentAccount;
    }
}

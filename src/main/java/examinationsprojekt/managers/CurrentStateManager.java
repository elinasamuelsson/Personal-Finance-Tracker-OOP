package examinationsprojekt.managers;

import examinationsprojekt.models.Account;
import examinationsprojekt.models.User;

public class CurrentStateManager {
    private static User currentUser;
    private static Account currentAccount;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void setCurrentUser(User user) {
        CurrentStateManager.currentUser = user;
    }

    public static void setCurrentAccount(Account account) {
        CurrentStateManager.currentAccount = account;
    }
}

package examinationsprojekt.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Account> accounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccountToList(Account account) {
        accounts.add(account);
    }

    public void removeAccountFromList(Account account) {
        accounts.remove(account);
    }
}

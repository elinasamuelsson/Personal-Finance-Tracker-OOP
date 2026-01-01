package examinationsprojekt.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
    final private UUID id;
    final private String username;
    final private String passwordHash;
    final private List<Account> accounts;

    public User(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.passwordHash = password;
        this.accounts = new ArrayList<>();
    }

    public User(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.passwordHash = password;
        this.accounts = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccountToList(Account account) {
        accounts.add(account);
    }

    public void updateExistingAccount(Account account) {
        for (Account a : accounts) {
            if (a.equals(account)) {
                a = account;
            }
        }
    }

    public void removeAccountFromList(Account account) {
        accounts.remove(account);
    }
}

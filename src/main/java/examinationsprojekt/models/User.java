package examinationsprojekt.models;

import examinationsprojekt.repositories.IUserRepository;
import examinationsprojekt.repositories.UserFileRepository;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
    final private UUID id;
    final private String username;
    final private String password;
    final private List<Account> accounts;

    public User(String username, String password) {
        this.id = UUID.randomUUID();
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

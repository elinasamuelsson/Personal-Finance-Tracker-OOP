package examinationsprojekt.models;

import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Account> accounts;

    public User(String username, String password, List<Account> accounts) {
        this.username = username;
        this.password = password;
        this.accounts = accounts;
    }
}

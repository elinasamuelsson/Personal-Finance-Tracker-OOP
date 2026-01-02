package examinationsprojekt.models;

import java.util.UUID;

public class CheckingAccount extends Account {
    public CheckingAccount(String name, AccountTypes type) {
        super(name, type);
    }

    public CheckingAccount(UUID id,String name, AccountTypes type, double balance) {
        super(id, name, type, balance);
    }
}

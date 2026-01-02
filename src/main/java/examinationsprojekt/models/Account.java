package examinationsprojekt.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Account implements Serializable {
    protected UUID id;
    protected final String name;
    protected final AccountTypes type;
    protected double balance;
    protected final ArrayList<Transaction> transactions;

    Account(String name, AccountTypes type) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.balance = 0;
        this.transactions = new ArrayList<Transaction>();
    }

    Account(UUID id, String name, AccountTypes type, double balance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountTypes getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance += balance;
    }

    public ArrayList<Transaction> getTransactionsCopy() {
        return new ArrayList<Transaction>(transactions);
    }

    public double getMonthlyInterestRate() {
        return 0.00;
    }

    public void addTransactionToList(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransactionFromList(Transaction transaction) {
        transactions.remove(transaction);
    }
}

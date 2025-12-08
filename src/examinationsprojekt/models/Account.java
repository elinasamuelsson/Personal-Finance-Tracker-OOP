package examinationsprojekt.models;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Account implements Serializable {
    protected final String name;
    protected final String owner;
    protected final AccountTypes type;
    protected double balance;
    protected final ArrayList<Transaction> transactions;

    Account(String name, String owner, AccountTypes type) {
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.balance = 0;
        this.transactions = new ArrayList<Transaction>();
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
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

    public void addTransactionToList(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransactionFromList(Transaction transaction) {
        transactions.remove(transaction);
    }
}

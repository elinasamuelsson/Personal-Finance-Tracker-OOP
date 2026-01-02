package examinationsprojekt.models;

import java.util.UUID;

public class SavingsAccount extends Account {
    private final double monthlyInterestRate;

    public SavingsAccount(String name, AccountTypes type, double interestRate) {
        super(name, type);
        this.monthlyInterestRate = interestRate;
    }

    public SavingsAccount(UUID id, String name, AccountTypes type, double balance, double monthlyInterestRate) {
        super(id,  name, type, balance);
        this.monthlyInterestRate = monthlyInterestRate;
    }

    @Override
    public double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public void monthlyInterestRatePayout() { //where to put this? add as meny option if selected account is savings?
        this.balance += (this.balance * monthlyInterestRate);
    }
}


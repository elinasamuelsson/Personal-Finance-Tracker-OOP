package examinationsprojekt.models;

public class SavingsAccount extends Account {
    private final double monthlyInterestRate;

    public SavingsAccount(String name, AccountTypes type, double interestRate) {
        super(name, type);
        this.monthlyInterestRate = interestRate;
    }

    public double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public void monthlyInterestRatePayout() { //where to put this? add as meny option if selected account is savings?
        this.balance += (this.balance * monthlyInterestRate);
    }
}


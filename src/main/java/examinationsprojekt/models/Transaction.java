package examinationsprojekt.models;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Transaction implements Serializable {
    private final String id;
    private final double amount;
    private final Instant time;
    private final TransactionTypes type;
    private final String description;
    private final boolean isEarning;

    public Transaction(double amount, Instant time, TransactionTypes type, String description, boolean isEarning) {
        this.isEarning = isEarning;
        this.id = idGenerator();
        this.amount = amount;
        this.time = time;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getLocalTime() {
        return LocalDateTime.ofInstant(time, ZoneOffset.UTC);
    }

    public TransactionTypes getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEarning() {
        return isEarning;
    }

    private String idGenerator() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            id.append(chars.charAt((int) (Math.random() * chars.length())));
        }

        return id.toString();
    }
}

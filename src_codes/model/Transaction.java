
package model;

import java.time.LocalDateTime;

// Represents a payment transaction for a charging session
public class Transaction {

    // Transaction details
    private final String transactionId;
    private final ChargingSession chargingSession;
    private final double amount;
    private final LocalDateTime timestamp;
    private String paymentStatus;

    // Constructor
    public Transaction(ChargingSession chargingSession, double amount) {

        if (chargingSession == null) {
            throw new IllegalArgumentException(
                "Charging session cannot be null."
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Transaction amount must be positive."
            );
        }

        this.transactionId = "TXN-" + System.currentTimeMillis();
        this.chargingSession = chargingSession;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.paymentStatus = "SUCCESS";
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public ChargingSession getChargingSession() {
        return chargingSession;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Display transaction details
    public void displayTransactionDetails() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Session ID: " + chargingSession.getSessionId());
        System.out.println("Amount: Rs. " + amount);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Payment Status: " + paymentStatus);
    }
}

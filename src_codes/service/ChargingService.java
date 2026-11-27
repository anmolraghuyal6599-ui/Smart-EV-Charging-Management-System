
package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.EVOwner;
import model.Vehicle;
import model.ChargingStation;
import model.ChargingPoint;
import model.ChargingSession;
import model.Transaction;

// Manages charging sessions and related transactions
public class ChargingService {

    private final List<ChargingSession> sessions;
    private final List<Transaction> transactions;

    // Constructor
    public ChargingService() {
        sessions = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    // Start a new charging session
    public ChargingSession startSession(
            EVOwner owner,
            int vehicleIndex,
            ChargingStation station,
            String pointId,
            double targetBatteryPercent) {

        if (owner == null || station == null) {
            throw new IllegalArgumentException(
                "Owner and station cannot be null."
            );
        }

        // Check whether the selected vehicle exists
        if (vehicleIndex < 0 ||
            vehicleIndex >= owner.getVehicles().size()) {

            throw new IllegalArgumentException(
                "Invalid vehicle selection."
            );
        }

        Vehicle vehicle = owner.getVehicles().get(vehicleIndex);

        if (vehicle == null) {
            throw new IllegalArgumentException(
                "Vehicle cannot be null."
            );
        }

        // Synchronize station access to prevent double booking
        synchronized (station) {

            // Find the selected charging point
            ChargingPoint point =
                station.findChargingPoint(pointId);

            if (point == null) {
                throw new IllegalArgumentException(
                    "Charging point not found."
                );
            }

            // Check availability
            if (!point.isAvailable()) {
                throw new IllegalStateException(
                    "Charging point is already occupied."
                );
            }

            // Reserve the charging point
            point.occupyPoint();

            try {
                // Create charging session
                ChargingSession session =
                    new ChargingSession(
                        owner,
                        vehicle,
                        station,
                        point,
                        targetBatteryPercent
                    );

                sessions.add(session);

                System.out.println(
                    "[SUCCESS] Charging session started: "
                    + session.getSessionId()
                );

                return session;

            } catch (RuntimeException e) {

                // Release point if session creation fails
                point.releasePoint();
                throw e;
            }
        }
    }

    // Find a charging session using its ID
    public ChargingSession findSessionById(String sessionId) {

        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Session ID cannot be empty."
            );
        }

        for (ChargingSession session : sessions) {

            if (session.getSessionId().equals(sessionId)) {
                return session;
            }
        }

        return null;
    }

    // End an active charging session
    public void endSession(String sessionId) {

        ChargingSession session = findSessionById(sessionId);

        if (session == null) {
            throw new IllegalArgumentException(
                "Charging session not found."
            );
        }

        synchronized (session) {

            if (session.isCompleted()) {
                System.out.println(
                    "[INFO] Session is already completed."
                );
                return;
            }

            // Complete the session
            session.completeSession();

            // Release the charging point
            session.getChargingPoint().releasePoint();

            System.out.println(
                "[SUCCESS] Charging session completed: "
                + session.getSessionId()
            );

            System.out.println(
                "[INFO] Charging point released successfully."
            );
        }
    }

    // Create a transaction for a completed session
    public Transaction createTransaction(
            String sessionId, double amount) {

        ChargingSession session = findSessionById(sessionId);

        if (session == null) {
            throw new IllegalArgumentException(
                "Charging session not found."
            );
        }

        if (!session.isCompleted()) {
            throw new IllegalStateException(
                "Complete the charging session before payment."
            );
        }

        for (Transaction transaction : transactions) {

            if (transaction.getChargingSession() == session) {
                throw new IllegalStateException(
                    "Transaction already exists for this session."
                );
            }
        }

        Transaction transaction =
            new Transaction(session, amount);

        transactions.add(transaction);

        System.out.println(
            "[SUCCESS] Transaction created: "
            + transaction.getTransactionId()
        );

        return transaction;
    }

    // Get all charging sessions
    public List<ChargingSession> getAllSessions() {

        return Collections.unmodifiableList(
            new ArrayList<>(sessions)
        );
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {

        return Collections.unmodifiableList(
            new ArrayList<>(transactions)
        );
    }

    // Display all charging sessions
    public void displayAllSessions() {

        if (sessions.isEmpty()) {
            System.out.println("No charging sessions available.");
            return;
        }

        for (ChargingSession session : sessions) {
            session.displaySessionDetails();
            System.out.println("----------------------");
        }
    }

    // Display all transactions
    public void displayAllTransactions() {

        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        for (Transaction transaction : transactions) {
            transaction.displayTransactionDetails();
            System.out.println("----------------------");
        }
    }
}

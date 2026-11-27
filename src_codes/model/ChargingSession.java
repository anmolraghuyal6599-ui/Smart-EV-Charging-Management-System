
package model;

import java.io.Serializable;
import java.time.LocalDateTime;

// Represents one EV charging session
public class ChargingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    // Session details
    private final String sessionId;
    private final EVOwner owner;
    private final Vehicle vehicle;
    private final ChargingStation station;
    private final ChargingPoint chargingPoint;

    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    // Charging progress
    private double targetBatteryPercent;
    private double energyDeliveredKwh;

    // Constructor
    public ChargingSession(EVOwner owner, Vehicle vehicle,
                           ChargingStation station,
                           ChargingPoint chargingPoint,
                           double targetBatteryPercent) {

        if (owner == null || vehicle == null ||
            station == null || chargingPoint == null) {
            throw new IllegalArgumentException(
                "Session details cannot be null."
            );
        }

        if (targetBatteryPercent <= 0 || targetBatteryPercent > 100) {
            throw new IllegalArgumentException(
                "Target battery percentage must be between 0 and 100."
            );
        }

        this.sessionId = "SES-" + System.currentTimeMillis();
        this.owner = owner;
        this.vehicle = vehicle;
        this.station = station;
        this.chargingPoint = chargingPoint;

        this.targetBatteryPercent = targetBatteryPercent;
        this.energyDeliveredKwh = 0.0;

        this.startTime = LocalDateTime.now();
        this.endTime = null;
        this.status = "ACTIVE";
    }

    // Getters
    public String getSessionId() {
        return sessionId;
    }

    public EVOwner getOwner() {
        return owner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ChargingStation getStation() {
        return station;
    }

    public ChargingPoint getChargingPoint() {
        return chargingPoint;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public double getTargetBatteryPercent() {
        return targetBatteryPercent;
    }

    public double getEnergyDeliveredKwh() {
        return energyDeliveredKwh;
    }

    // Add energy delivered during charging
    public void addEnergyUnits(double energy) {
        if (energy <= 0) {
            throw new IllegalArgumentException(
                "Energy must be positive."
            );
        }

        energyDeliveredKwh += energy;
    }

    // Check whether the session is completed
    public boolean isCompleted() {
        return status.equals("COMPLETED");
    }

    // Complete the charging session
    public void completeSession() {
        if (!isCompleted()) {
            status = "COMPLETED";
            endTime = LocalDateTime.now();
        }
    }

    // Display session details
    public void displaySessionDetails() {
        System.out.println("Session ID: " + sessionId);
        System.out.println("Owner: " + owner.getName());
        System.out.println("Vehicle: " + vehicle.getModel());
        System.out.println("Charging Point: " + chargingPoint.getPointId());
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Status: " + status);
        System.out.println("Energy Delivered: " + energyDeliveredKwh + " kWh");
    }
}


package model;

import java.io.Serializable;

// Abstract parent class for different EV types
public abstract class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    // Encapsulation
    private final String vehicleId;
    private String model;
    private double batteryCapacity;

    // Constructor
    public Vehicle(String vehicleId, String model, double batteryCapacity) {

        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }

        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle model cannot be empty.");
        }

        if (batteryCapacity <= 0) {
            throw new IllegalArgumentException("Battery capacity must be positive.");
        }

        this.vehicleId = vehicleId;
        this.model = model;
        this.batteryCapacity = batteryCapacity;
    }

    // Getters
    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    // Setters
    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle model cannot be empty.");
        }
        this.model = model;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        if (batteryCapacity <= 0) {
            throw new IllegalArgumentException("Battery capacity must be positive.");
        }
        this.batteryCapacity = batteryCapacity;
    }

    // Abstract method
    public abstract void displayVehicleDetails();
}


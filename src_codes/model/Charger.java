
package model;

// Abstract parent class for different charger types
public abstract class Charger {

    // Common charger details
    private final String chargerId;
    private double chargingPower;

    // Constructor
    public Charger(String chargerId, double chargingPower) {

        if (chargerId == null || chargerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Charger ID cannot be empty.");
        }

        if (chargingPower <= 0) {
            throw new IllegalArgumentException("Charging power must be positive.");
        }

        this.chargerId = chargerId;
        this.chargingPower = chargingPower;
    }

    // Getters
    public String getChargerId() {
        return chargerId;
    }

    public double getChargingPower() {
        return chargingPower;
    }

    // Setter
    public void setChargingPower(double chargingPower) {
        if (chargingPower <= 0) {
            throw new IllegalArgumentException("Charging power must be positive.");
        }

        this.chargingPower = chargingPower;
    }

    // Abstract method
    public abstract void displayChargerDetails();
}


package model;

// ElectricBike inherits from Vehicle
public class ElectricBike extends Vehicle {

    private double rangePerCharge;

    // Constructor
    public ElectricBike(String vehicleId, String model,
                        double batteryCapacity, double rangePerCharge) {

        super(vehicleId, model, batteryCapacity);

        if (rangePerCharge <= 0) {
            throw new IllegalArgumentException("Range must be positive.");
        }

        this.rangePerCharge = rangePerCharge;
    }

    // Getter
    public double getRangePerCharge() {
        return rangePerCharge;
    }

    // Setter
    public void setRangePerCharge(double rangePerCharge) {
        if (rangePerCharge <= 0) {
            throw new IllegalArgumentException("Range must be positive.");
        }

        this.rangePerCharge = rangePerCharge;
    }

    // Override abstract method from Vehicle
    @Override
    public void displayVehicleDetails() {
        System.out.println("Vehicle Type: Electric Bike");
        System.out.println("Vehicle ID: " + getVehicleId());
        System.out.println("Model: " + getModel());
        System.out.println("Battery Capacity: " + getBatteryCapacity() + " kWh");
        System.out.println("Range Per Charge: " + rangePerCharge + " km");
    }
}


package model;

// ElectricCar inherits from Vehicle
public class ElectricCar extends Vehicle {

    private int numberOfDoors;

    // Constructor
    public ElectricCar(String vehicleId, String model,
                       double batteryCapacity, int numberOfDoors) {

        super(vehicleId, model, batteryCapacity);

        if (numberOfDoors <= 0) {
            throw new IllegalArgumentException("Number of doors must be positive.");
        }

        this.numberOfDoors = numberOfDoors;
    }

    // Getter
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    // Setter
    public void setNumberOfDoors(int numberOfDoors) {
        if (numberOfDoors <= 0) {
            throw new IllegalArgumentException("Number of doors must be positive.");
        }

        this.numberOfDoors = numberOfDoors;
    }

    // Override abstract method from Vehicle
    @Override
    public void displayVehicleDetails() {
        System.out.println("Vehicle Type: Electric Car");
        System.out.println("Vehicle ID: " + getVehicleId());
        System.out.println("Model: " + getModel());
        System.out.println("Battery Capacity: " + getBatteryCapacity() + " kWh");
        System.out.println("Number of Doors: " + numberOfDoors);
    }
}

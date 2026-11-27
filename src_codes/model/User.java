
package model;

import java.util.ArrayList;
import java.util.List;

// User inherits from Account
public class User extends Account {

    // A user can own multiple vehicles
    private final List<Vehicle> vehicles;

    // Constructor
    public User(String userId, String name, String contact) {
        super(userId, name, contact);
        vehicles = new ArrayList<>();
    }

    // Add a vehicle to the user's list
    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            vehicles.add(vehicle);
        }
    }

    // Get all vehicles
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    // Display user role
    @Override
    public void displayRole() {
        System.out.println("Role: EV User");
    }

    // Display user details
    public void displayUserDetails() {
        displayAccountDetails();
        System.out.println("Total Vehicles: " + vehicles.size());
    }
}

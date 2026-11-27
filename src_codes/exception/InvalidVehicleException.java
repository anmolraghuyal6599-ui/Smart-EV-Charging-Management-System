
package exception;

// Thrown when an invalid or unregistered vehicle is provided
public class InvalidVehicleException extends Exception {

    private final String vehicleDetails;

    public InvalidVehicleException(String message) {
        super(message);
        this.vehicleDetails = message;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }
}

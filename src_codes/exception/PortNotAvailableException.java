
package exception;

// Thrown when a charging point is occupied or unavailable
public class PortNotAvailableException extends Exception {

    private final String portId;
    private final String currentStatus;

    public PortNotAvailableException(String portId, String currentStatus) {

        super("Charging Port [" + portId
                + "] is unavailable. Current Status: " + currentStatus);

        this.portId = portId;
        this.currentStatus = currentStatus;
    }

    public String getPortId() {
        return portId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }
}

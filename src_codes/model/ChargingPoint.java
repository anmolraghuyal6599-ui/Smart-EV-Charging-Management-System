
package model;

import java.io.Serializable;

// Represents an individual charging point
public class ChargingPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    // Charging point details
    private final String pointId;
    private String connectorType;
    private String status;

    // Constructor
    public ChargingPoint(String pointId, String connectorType) {

        if (pointId == null || pointId.trim().isEmpty()) {
            throw new IllegalArgumentException("Point ID cannot be empty.");
        }

        if (connectorType == null || connectorType.trim().isEmpty()) {
            throw new IllegalArgumentException("Connector type cannot be empty.");
        }

        this.pointId = pointId;
        this.connectorType = connectorType;
        this.status = "Available";
    }

    // Getters
    public String getPointId() {
        return pointId;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public String getStatus() {
        return status;
    }

    // Check whether the charging point is available
    public boolean isAvailable() {
        return status.equals("Available");
    }

    // Occupy the charging point
    public void occupyPoint() {

        if (!isAvailable()) {
            throw new IllegalStateException("Charging point is already occupied.");
        }

        status = "Occupied";
    }

    // Release the charging point
    public void releasePoint() {
        status = "Available";
    }

    // Display charging point details
    public void displayPointDetails() {

        System.out.println("Point ID: " + pointId);
        System.out.println("Connector Type: " + connectorType);
        System.out.println("Status: " + status);
    }
}

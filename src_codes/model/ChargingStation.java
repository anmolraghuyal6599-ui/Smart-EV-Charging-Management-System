
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Represents an EV charging station
public class ChargingStation implements Serializable {

    private static final long serialVersionUID = 1L;

    // Station details
    private final String stationId;
    private String name;
    private String location;

    // A station can contain multiple charging points
    private List<ChargingPoint> chargingPoints;

    // Constructor
    public ChargingStation(String stationId, String name, String location) {

        if (stationId == null || stationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Station ID cannot be empty.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Station name cannot be empty.");
        }

        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }

        this.stationId = stationId;
        this.name = name;
        this.location = location;

        chargingPoints = new ArrayList<>();
    }

    // Getters
    public String getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    // Add a charging point to the station
    public void addChargingPoint(ChargingPoint point) {

        if (point != null) {
            chargingPoints.add(point);
        }
    }

    // Find a charging point using its ID
    public ChargingPoint findChargingPoint(String pointId) {

        for (ChargingPoint point : chargingPoints) {

            if (point.getPointId().equals(pointId)) {
                return point;
            }
        }

        return null;
    }

    // Display station details
    public void displayStationDetails() {

        System.out.println("Station ID: " + stationId);
        System.out.println("Station Name: " + name);
        System.out.println("Location: " + location);
        System.out.println("Total Charging Points: " + chargingPoints.size());
    }
}



package util;

import model.ChargingPoint;
import model.ChargingStation;

// Generates a performance report for a charging station
public class ReportGenerator {

    // Creates and returns the station performance report
    public static String generateStationPerformanceReport(ChargingStation station) {

        StringBuilder sb = new StringBuilder(1024);

        sb.append("====================================================\n");
        sb.append("          CHARGING STATION PERFORMANCE REPORT\n");
        sb.append("====================================================\n");

        sb.append("Station: ").append(station.getName())
          .append(" (ID: ").append(station.getStationId()).append(")\n");

        sb.append("Location: ").append(station.getLocation()).append("\n");

        sb.append("----------------------------------------------------\n");

        // Displays details of every charging point
        for (ChargingPoint point : station.getChargingPoints()) {

            sb.append("Point ID: ").append(point.getPointId())
              .append(" | Connector: ").append(point.getConnectorType())
              .append(" | Status: ").append(point.getStatus())
              .append("\n");
        }

        sb.append("----------------------------------------------------\n");

        sb.append("Total Charging Points: ")
          .append(station.getChargingPoints().size()).append("\n");

        sb.append("====================================================\n");

        return sb.toString();
    }
}

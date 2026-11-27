
package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// Saves generated station reports and customer receipts
public class ReportSaver {

    // Saves report content into a file
    public static boolean saveReport(String fileName, String reportContent) {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty.");
        }

        if (reportContent == null) {
            throw new IllegalArgumentException("Report content cannot be null.");
        }

        try (PrintWriter writer =
                new PrintWriter(new FileWriter(fileName))) {

            writer.println(reportContent);

            System.out.println("Report saved successfully!");
            System.out.println("File location: " + fileName);

            return true;

        } catch (IOException e) {

            System.out.println("Error saving report: " + e.getMessage());

            return false;
        }
    }

    // Saves a station performance report
    public static boolean saveStationReport(
            model.ChargingStation station, String fileName) {

        String report =
                ReportGenerator.generateStationPerformanceReport(station);

        return saveReport(fileName, report);
    }

    // Saves customer receipt or transaction details
    public static boolean saveReceipt(
            String fileName, String receiptContent) {

        return saveReport(fileName, receiptContent);
    }
}

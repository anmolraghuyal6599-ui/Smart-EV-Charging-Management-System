
package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Loads previously stored data from text files
public class DataLoader {

    // Reads all lines from a specified file
    public static List<String> loadData(String fileName) {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty.");
        }

        List<String> records = new ArrayList<>();

        // Opens the file and reads it line by line
        try (BufferedReader reader =
                new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.trim().isEmpty()) {
                    records.add(line);
                }
            }

            System.out.println("Data loaded successfully from: " + fileName);
            System.out.println("Total records loaded: " + records.size());

        } catch (IOException e) {

            System.out.println("Error while loading data: " + e.getMessage());
        }

        return records;
    }

    // Displays all records loaded from a file
    public static void displayData(String fileName) {

        List<String> records = loadData(fileName);

        System.out.println("========== STORED DATA ==========");

        for (String record : records) {
            System.out.println(record);
        }

        System.out.println("=================================");
    }
}

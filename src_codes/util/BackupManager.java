
package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Creates backup copies of important EV system data files
public class BackupManager {

    // Copies an original file to a backup destination
    public static boolean createBackup(String sourceFile, String backupFile) {

        if (sourceFile == null || sourceFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Source file cannot be empty.");
        }

        if (backupFile == null || backupFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Backup file cannot be empty.");
        }

        // Prevent copying a file onto itself
        if (sourceFile.equals(backupFile)) {
            throw new IllegalArgumentException(
                    "Source and backup files cannot be the same.");
        }

        // Opens the original file and creates the backup file
        try (
            FileInputStream input = new FileInputStream(sourceFile);
            FileOutputStream output = new FileOutputStream(backupFile)
        ) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            // Copies data in blocks until the entire file is copied
            while ((bytesRead = input.read(buffer)) != -1) {

                output.write(buffer, 0, bytesRead);
            }

            System.out.println("Backup created successfully!");
            System.out.println("Original file: " + sourceFile);
            System.out.println("Backup file: " + backupFile);

            return true;

        } catch (IOException e) {

            System.out.println("Error creating backup: " + e.getMessage());

            return false;
        }
    }
}

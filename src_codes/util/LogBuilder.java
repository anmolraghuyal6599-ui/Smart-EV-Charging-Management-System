
package util;

// Maintains a thread-safe activity log for the EV charging system
public class LogBuilder {

    // Shared log storage
    private static final StringBuffer log = new StringBuffer();

    // Private constructor prevents unnecessary object creation
    private LogBuilder() {
    }

    // Adds a new activity to the log
    public static synchronized void addLog(String activity) {

        if (activity == null || activity.trim().isEmpty()) {
            throw new IllegalArgumentException("Log activity cannot be empty.");
        }

        log.append("[LOG] ")
           .append(activity)
           .append("\n");

        System.out.println("[LOG] " + activity);
    }

    // Returns all recorded activities
    public static synchronized String getLogs() {

        return log.toString();
    }

    // Displays all recorded activities
    public static synchronized void displayLogs() {

        System.out.println("========== SYSTEM ACTIVITY LOG ==========");
        System.out.println(log.toString());
        System.out.println("=========================================");
    }

    // Clears the activity log
    public static synchronized void clearLogs() {

        log.setLength(0);

        System.out.println("[LOG] Activity history cleared.");
    }
}

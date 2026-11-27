import gui.MainFrame;
import javax.swing.SwingUtilities;
import service.ChargingService;
import service.ThreadManager;
import service.UserService;

public class main {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Smart EV Charging Management System");
        System.out.println(" Group 16 | NIET Greater Noida");
        System.out.println("====================================");

        SwingUtilities.invokeLater(() -> {
            try {
                UserService userService = new UserService();
                ChargingService chargingService = new ChargingService();
                ThreadManager threadManager = new ThreadManager();

                MainFrame frame = new MainFrame(userService, chargingService, threadManager);
                frame.setVisible(true);

                System.out.println("[GUI] Application launched successfully.");

            } catch (Exception ex) {
                System.out.println("Unable to start Smart EV Charging Management System.");
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}


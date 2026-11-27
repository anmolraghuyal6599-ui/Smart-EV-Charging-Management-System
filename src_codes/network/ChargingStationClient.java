
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// Represents a charging station communicating with the network server
public class ChargingStationClient {

    private final String serverHost;
    private final int serverPort;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    // Constructor
    public ChargingStationClient(String serverHost, int serverPort) {

        if (serverHost == null || serverHost.trim().isEmpty()) {
            throw new IllegalArgumentException("Server host cannot be empty.");
        }

        if (serverPort < 1 || serverPort > 65535) {
            throw new IllegalArgumentException("Invalid server port.");
        }

        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    // Connects the charging station to the network server
    public void connect() throws IOException {

        socket = new Socket(serverHost, serverPort);

        reader = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8
                )
        );

        writer = new PrintWriter(
                socket.getOutputStream(),
                true,
                StandardCharsets.UTF_8
        );

        System.out.println("Connected to EV Network Server.");
    }

    // Sends charging station status to the server
    public String sendStatus(String stationId, String status) throws IOException {

        if (stationId == null || stationId.trim().isEmpty()
                || status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Station ID and status cannot be empty.");
        }

        return sendMessage("STATUS|" + stationId + "|" + status);
    }

    // Sends charging usage information to the server
    public String sendUsage(String stationId, String usageSummary) throws IOException {

        if (stationId == null || stationId.trim().isEmpty()
                || usageSummary == null || usageSummary.trim().isEmpty()) {
            throw new IllegalArgumentException("Station ID and usage summary cannot be empty.");
        }

        return sendMessage("USAGE|" + stationId + "|" + usageSummary);
    }

    // Sends a message and receives the server response
    private String sendMessage(String message) throws IOException {

        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            throw new IllegalStateException("Client is not connected to the server.");
        }

        writer.println(message);

        String response = reader.readLine();

        if (response == null) {
            throw new IOException("Server disconnected without sending a response.");
        }

        System.out.println("Server Response: " + response);

        return response;
    }

    // Disconnects the charging station from the server
    public void disconnect() {

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            System.out.println("Disconnected from EV Network Server.");

        } catch (IOException e) {
            System.out.println("Error disconnecting client: " + e.getMessage());
        }
    }
}

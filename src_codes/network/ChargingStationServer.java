
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import util.LogBuilder;

// Manages network communication with EV charging stations
public class ChargingStationServer implements AutoCloseable {

    public static final int PORT = 5000;

    private ServerSocket serverSocket;

    // Manages multiple connected charging stations
    private final ExecutorService clientPool =
            Executors.newCachedThreadPool();

    private volatile boolean running = false;

    // Starts the charging station server
    public void startServer() throws IOException {

        serverSocket = new ServerSocket(PORT);
        running = true;

        System.out.println("Charging Station Server started.");
        System.out.println("Listening on port: " + PORT);

        LogBuilder.addLog(
                "Charging Station Server started on port " + PORT
        );

        try {

            while (running) {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "Charging station connected: "
                        + clientSocket.getInetAddress()
                );

                // Handles each client using a separate thread
                clientPool.execute(new ClientHandler(clientSocket));
            }

        } catch (SocketException e) {

            if (running) {
                throw e;
            }

        } finally {

            running = false;
            clientPool.shutdown();
        }
    }

    // Handles communication with an individual client
    private class ClientHandler implements Runnable {

        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try (
                Socket socket = clientSocket;

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );

                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(),
                        true,
                        StandardCharsets.UTF_8
                )
            ) {

                String message;

                while ((message = reader.readLine()) != null) {

                    System.out.println("Received: " + message);

                    String[] parts = message.split("\\|", 3);

                    if (parts.length == 3
                            && !parts[1].trim().isEmpty()
                            && !parts[2].trim().isEmpty()) {

                        if (parts[0].equalsIgnoreCase("STATUS")) {

                            System.out.println(
                                    "Station ID: " + parts[1]
                            );

                            System.out.println(
                                    "Station Status: " + parts[2]
                            );

                            writer.println("ACK|STATUS");

                            LogBuilder.addLog(
                                    "Received STATUS from station "
                                    + parts[1]
                            );

                        } else if (parts[0].equalsIgnoreCase("USAGE")) {

                            System.out.println(
                                    "Station ID: " + parts[1]
                            );

                            System.out.println(
                                    "Usage / Revenue Summary: " + parts[2]
                            );

                            writer.println("ACK|USAGE");

                            LogBuilder.addLog(
                                    "Received USAGE from station "
                                    + parts[1]
                            );

                        } else {

                            writer.println(
                                    "ERROR|Unknown message type"
                            );
                        }

                    } else {

                        writer.println(
                                "ERROR|Invalid message format"
                        );
                    }
                }

            } catch (IOException e) {

                System.out.println(
                        "Client communication error: "
                        + e.getMessage()
                );
            }
        }
    }

    // Stops the server and closes its connection
    @Override
    public void close() {

        running = false;

        try {

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error closing server: " + e.getMessage()
            );
        }

        clientPool.shutdown();

        System.out.println("Charging Station Server stopped.");
    }
}

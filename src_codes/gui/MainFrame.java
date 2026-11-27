package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Account;
import model.ChargingPoint;
import model.ChargingStation;
import model.ElectricBike;
import model.ElectricCar;
import model.User;
import model.Vehicle;
import service.ChargingService;
import service.ThreadManager;
import service.UserService;
import storage.FileDataStore;
import util.ReportGenerator;
import util.ReportSaver;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color SIDEBAR = new Color(35, 47, 62);

    private final UserService userService;
    private final ChargingService chargingService;
    private final ThreadManager threadManager;

    private final List<ChargingStation> stations;
    private final FileDataStore<ChargingStation> stationStore;

    private JLabel totalUsersLabel;
    private JLabel totalVehiclesLabel;
    private JLabel totalStationsLabel;
    private JLabel totalPointsLabel;

    public MainFrame(UserService userService, ChargingService chargingService, ThreadManager threadManager) {
        this.userService = userService;
        this.chargingService = chargingService;
        this.threadManager = threadManager;

        this.stations = new ArrayList<>();
        this.stationStore = new FileDataStore<>("data/stations.dat");

        try {
            this.stations.addAll(stationStore.load());
            System.out.println("[DATA] Loaded stations: " + stations.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Loading station data: " + e.getMessage());
        }

        setTitle("Smart EV Charging Management System");
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(createDashboard(), "Dashboard");
        contentPanel.add(createUserScreen(), "Users");
        contentPanel.add(createVehicleScreen(), "Vehicles");
        contentPanel.add(createStationScreen(), "Stations");
        contentPanel.add(createChargingScreen(), "Charging");
        contentPanel.add(createReportScreen(), "Reports");
        contentPanel.add(createNetworkScreen(), "Network");

        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "Dashboard");
        updateDashboardCounts();
    }

    private void saveStations() {
        try {
            stationStore.save(stations);
            System.out.println("[DATA] Stations and charging points saved.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save station data: " + e.getMessage(), "Storage Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 12, 20, 12));

        JLabel title = new JLabel("EV CHARGING");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Management System");
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(35));

        addNavButton(sidebar, "Dashboard", "Dashboard");
        addNavButton(sidebar, "User Management", "Users");
        addNavButton(sidebar, "Vehicle Management", "Vehicles");
        addNavButton(sidebar, "Charging Stations", "Stations");
        addNavButton(sidebar, "Charging Operations", "Charging");
        addNavButton(sidebar, "Reports", "Reports");
        addNavButton(sidebar, "Network Monitoring", "Network");

        sidebar.add(Box.createVerticalGlue());

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) System.exit(0);
        });
        sidebar.add(exitButton);
        return sidebar;
    }

    private void addNavButton(JPanel sidebar, String label, String screen) {
        JButton button = new JButton(label);
        styleButton(button);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            if ("Dashboard".equals(screen)) updateDashboardCounts();
            cardLayout.show(contentPanel, screen);
        });
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(10));
    }

    private void styleButton(JButton button) {
        button.setMaximumSize(new Dimension(195, 42));
        button.setPreferredSize(new Dimension(195, 42));
        button.setBackground(new Color(55, 70, 88));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createBaseScreen(String heading) {
        JPanel panel = new JPanel(new BorderLayout(15, 20));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        JLabel title = new JLabel(heading);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(new Color(35, 47, 62));
        panel.add(title, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createDashboard() {
        JPanel panel = createBaseScreen("Dashboard");
        JPanel center = new JPanel(new GridLayout(2, 2, 20, 20));
        center.setBackground(BACKGROUND);
        center.add(createInfoCard("Total Users", "Registered users", true));
        center.add(createInfoCard("Total Vehicles", "Registered electric vehicles", false));
        center.add(createInfoCard("Total Stations", "Charging stations added", false));
        center.add(createInfoCard("Total Charging Points", "Charging points across stations", false));
        panel.add(center, BorderLayout.CENTER);
        JLabel note = new JLabel("Welcome to Smart EV Charging Management System");
        note.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(note, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createInfoCard(String heading, String description, boolean firstCard) {
        JPanel card = new JPanel(new GridLayout(3, 1, 5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(215, 220, 225)), new EmptyBorder(20, 20, 20, 20)));
        JLabel title = new JLabel(heading);
        title.setFont(new Font("Arial", Font.BOLD, 19));
        JLabel value = new JLabel("0");
        value.setFont(new Font("Arial", Font.BOLD, 30));
        value.setForeground(new Color(35, 100, 160));
        JLabel detail = new JLabel(description);
        detail.setForeground(Color.GRAY);

        if (firstCard) totalUsersLabel = value;
        else if (totalVehiclesLabel == null) totalVehiclesLabel = value;
        else if (totalStationsLabel == null) totalStationsLabel = value;
        else totalPointsLabel = value;

        card.add(title);
        card.add(value);
        card.add(detail);
        return card;
    }

    private void updateDashboardCounts() {
        int userCount = 0, vehicleCount = 0, stationCount = stations.size(), pointCount = 0;
        for (Account account : userService.getAllAccounts()) {
            if (account instanceof User) {
                userCount++;
                User user = (User) account;
                vehicleCount += user.getVehicles().size();
            }
        }
        for (ChargingStation station : stations) {
            pointCount += station.getChargingPoints().size();
        }
        if (totalUsersLabel != null) totalUsersLabel.setText(String.valueOf(userCount));
        if (totalVehiclesLabel != null) totalVehiclesLabel.setText(String.valueOf(vehicleCount));
        if (totalStationsLabel != null) totalStationsLabel.setText(String.valueOf(stationCount));
        if (totalPointsLabel != null) totalPointsLabel.setText(String.valueOf(pointCount));
    }

    private JPanel createUserScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("User Management");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        String[] columns = {"User ID", "Name", "Contact"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField contactField = new JTextField(10);

        JButton addButton = new JButton("Add User");
        JButton deleteButton = new JButton("Delete User");
        JButton refreshButton = new JButton("Refresh");

        JPanel inputPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        inputPanel.add(new JLabel("User ID"));
        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Contact"));
        inputPanel.add(idField);
        inputPanel.add(nameField);
        inputPanel.add(contactField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        Runnable refreshTable = () -> {
            tableModel.setRowCount(0);
            for (Account account : userService.getAllAccounts()) {
                if (account instanceof User) {
                    tableModel.addRow(new Object[]{account.getAccountId(), account.getName(), account.getContact()});
                }
            }
        };

        addButton.addActionListener(e -> {
            try {
                String userId = idField.getText().trim();
                String name = nameField.getText().trim();
                String contact = contactField.getText().trim();
                if (userId.isEmpty() || name.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please fill all fields.");
                    return;
                }
                userService.registerUser(new User(userId, name, contact));
                refreshTable.run();
                updateDashboardCounts();
                idField.setText(""); nameField.setText(""); contactField.setText("");
                JOptionPane.showMessageDialog(panel, "User registered successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a user first.");
                return;
            }
            String userId = tableModel.getValueAt(selectedRow, 0).toString();
            if (userService.removeAccount(userId)) {
                refreshTable.run();
                updateDashboardCounts();
                JOptionPane.showMessageDialog(panel, "User deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(panel, "User not found.");
            }
        });

        refreshButton.addActionListener(e -> { refreshTable.run(); updateDashboardCounts(); });

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        refreshTable.run();
        return panel;
    }

    private JPanel createVehicleScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Vehicle Management");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JComboBox<String> userCombo = new JComboBox<>();
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(title, BorderLayout.NORTH);
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("Select User:"));
        userPanel.add(userCombo);
        topPanel.add(userPanel, BorderLayout.SOUTH);

        String[] columns = {"Vehicle ID", "Type", "Model", "Battery (kWh)", "Doors / Range"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable vehicleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        JTextField idField = new JTextField(10);
        JTextField modelField = new JTextField(10);
        JTextField batteryField = new JTextField(10);
        JTextField extraField = new JTextField(10);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Electric Car", "Electric Bike"});
        JLabel extraLabel = new JLabel("Number of Doors:");

        typeCombo.addActionListener(e -> {
            if ("Electric Car".equals(typeCombo.getSelectedItem())) extraLabel.setText("Number of Doors:");
            else extraLabel.setText("Range per Charge (km):");
        });

        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 8, 8));
        inputPanel.add(new JLabel("Vehicle ID"));
        inputPanel.add(new JLabel("Model"));
        inputPanel.add(new JLabel("Battery Capacity"));
        inputPanel.add(new JLabel("Vehicle Type"));
        inputPanel.add(extraLabel);
        inputPanel.add(idField);
        inputPanel.add(modelField);
        inputPanel.add(batteryField);
        inputPanel.add(typeCombo);
        inputPanel.add(extraField);

        JButton addButton = new JButton("Add Vehicle");
        JButton deleteButton = new JButton("Delete Vehicle");
        JButton refreshButton = new JButton("Refresh");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        Runnable refreshUsers = () -> {
            String previousSelection = (String) userCombo.getSelectedItem();
            userCombo.removeAllItems();
            for (Account account : userService.getAllAccounts()) {
                if (account instanceof User) userCombo.addItem(account.getAccountId());
            }
            if (previousSelection != null) userCombo.setSelectedItem(previousSelection);
        };

        Runnable refreshTable = () -> {
            tableModel.setRowCount(0);
            String selectedId = (String) userCombo.getSelectedItem();
            if (selectedId == null) return;
            for (Account account : userService.getAllAccounts()) {
                if (account instanceof User && account.getAccountId().equals(selectedId)) {
                    User selectedUser = (User) account;
                    for (Vehicle vehicle : selectedUser.getVehicles()) {
                        String type = "Vehicle";
                        String extra = "N/A";
                        if (vehicle instanceof ElectricCar) {
                            ElectricCar car = (ElectricCar) vehicle;
                            type = "Electric Car";
                            extra = String.valueOf(car.getNumberOfDoors());
                        } else if (vehicle instanceof ElectricBike) {
                            ElectricBike bike = (ElectricBike) vehicle;
                            type = "Electric Bike";
                            extra = String.valueOf(bike.getRangePerCharge());
                        }
                        tableModel.addRow(new Object[]{vehicle.getVehicleId(), type, vehicle.getModel(), vehicle.getBatteryCapacity(), extra});
                    }
                    break;
                }
            }
        };

        userCombo.addActionListener(e -> refreshTable.run());

        addButton.addActionListener(e -> {
            try {
                String userId = (String) userCombo.getSelectedItem();
                if (userId == null) { JOptionPane.showMessageDialog(panel, "Please register a user first."); return; }
                String vehicleId = idField.getText().trim();
                String model = modelField.getText().trim();
                String batteryText = batteryField.getText().trim();
                String extraText = extraField.getText().trim();
                if (vehicleId.isEmpty() || model.isEmpty() || batteryText.isEmpty() || extraText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please fill all vehicle details."); return;
                }
                double battery = Double.parseDouble(batteryText);
                User selectedUser = null;
                for (Account account : userService.getAllAccounts()) {
                    if (account instanceof User && account.getAccountId().equals(userId)) { selectedUser = (User) account; break; }
                }
                if (selectedUser == null) { JOptionPane.showMessageDialog(panel, "Selected user not found."); return; }
                for (Vehicle existing : selectedUser.getVehicles()) {
                    if (existing.getVehicleId().equals(vehicleId)) { JOptionPane.showMessageDialog(panel, "Vehicle ID already exists for this user."); return; }
                }
                Vehicle vehicle;
                if ("Electric Car".equals(typeCombo.getSelectedItem())) {
                    vehicle = new ElectricCar(vehicleId, model, battery, Integer.parseInt(extraText));
                } else {
                    vehicle = new ElectricBike(vehicleId, model, battery, Double.parseDouble(extraText));
                }
                selectedUser.addVehicle(vehicle);
                userService.saveChanges();
                refreshTable.run();
                updateDashboardCounts();
                idField.setText(""); modelField.setText(""); batteryField.setText(""); extraField.setText("");
                JOptionPane.showMessageDialog(panel, "Vehicle registered successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Enter valid numeric values.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = vehicleTable.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(panel, "Please select a vehicle first."); return; }
            String userId = (String) userCombo.getSelectedItem();
            if (userId == null) return;
            String vehicleId = tableModel.getValueAt(selectedRow, 0).toString();

            for (Account account : userService.getAllAccounts()) {
                if (account instanceof User && account.getAccountId().equals(userId)) {
                    User selectedUser = (User) account;
                    List<Vehicle> vehicles = selectedUser.getVehicles();
                    boolean removed = false;
                    for (int i = 0; i < vehicles.size(); i++) {
                        if (vehicles.get(i).getVehicleId().equals(vehicleId)) {
                            vehicles.remove(i);
                            removed = true;
                            break;
                        }
                    }
                    if (removed) {
                        userService.saveChanges();
                        refreshTable.run();
                        updateDashboardCounts();
                        JOptionPane.showMessageDialog(panel, "Vehicle deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(panel, "Vehicle not found.");
                    }
                    return;
                }
            }
        });

        refreshButton.addActionListener(e -> { refreshUsers.run(); refreshTable.run(); updateDashboardCounts(); });
        refreshUsers.run();
        refreshTable.run();

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStationScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Charging Station Management");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        String[] stationColumns = {"Station ID", "Station Name", "Location", "Total Points"};
        DefaultTableModel stationModel = new DefaultTableModel(stationColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable stationTable = new JTable(stationModel);
        JScrollPane stationScroll = new JScrollPane(stationTable);
        stationScroll.setBorder(BorderFactory.createTitledBorder("Charging Stations"));

        String[] pointColumns = {"Point ID", "Connector Type", "Status"};
        DefaultTableModel pointModel = new DefaultTableModel(pointColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable pointTable = new JTable(pointModel);
        JScrollPane pointScroll = new JScrollPane(pointTable);
        pointScroll.setBorder(BorderFactory.createTitledBorder("Charging Points"));

        JTextField stationIdField = new JTextField(10);
        JTextField stationNameField = new JTextField(10);
        JTextField locationField = new JTextField(10);
        JButton addStationButton = new JButton("Add Station");
        JButton deleteStationButton = new JButton("Delete Station");
        JButton refreshButton = new JButton("Refresh");

        JTextField pointIdField = new JTextField(10);
        JTextField connectorField = new JTextField(10);
        JButton addPointButton = new JButton("Add Charging Point");
        JButton deletePointButton = new JButton("Delete Point");

        Runnable refreshPointTable = () -> {
            pointModel.setRowCount(0);
            int selectedRow = stationTable.getSelectedRow();
            if (selectedRow == -1) return;
            String selectedId = stationModel.getValueAt(selectedRow, 0).toString();
            for (ChargingStation station : stations) {
                if (station.getStationId().equals(selectedId)) {
                    for (ChargingPoint point : station.getChargingPoints()) {
                        pointModel.addRow(new Object[]{point.getPointId(), point.getConnectorType(), point.getStatus()});
                    }
                    break;
                }
            }
        };

        Runnable refreshStationTable = () -> {
            String previousId = null;
            int selectedRow = stationTable.getSelectedRow();
            if (selectedRow != -1) previousId = stationModel.getValueAt(selectedRow, 0).toString();
            stationModel.setRowCount(0);
            for (ChargingStation station : stations) {
                stationModel.addRow(new Object[]{station.getStationId(), station.getName(), station.getLocation(), station.getChargingPoints().size()});
            }
            int rowToSelect = -1;
            for (int i = 0; i < stationModel.getRowCount(); i++) {
                if (previousId != null && stationModel.getValueAt(i, 0).toString().equals(previousId)) { rowToSelect = i; break; }
            }
            if (rowToSelect == -1 && stationModel.getRowCount() > 0) rowToSelect = 0;
            if (rowToSelect != -1) stationTable.setRowSelectionInterval(rowToSelect, rowToSelect);
            else pointModel.setRowCount(0);
            updateDashboardCounts();
        };

        stationTable.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) refreshPointTable.run(); });

        addStationButton.addActionListener(e -> {
            try {
                String stationId = stationIdField.getText().trim();
                String name = stationNameField.getText().trim();
                String location = locationField.getText().trim();
                if (stationId.isEmpty() || name.isEmpty() || location.isEmpty()) { JOptionPane.showMessageDialog(panel, "Please fill all station details."); return; }
                for (ChargingStation station : stations) {
                    if (station.getStationId().equals(stationId)) { JOptionPane.showMessageDialog(panel, "Station ID already exists."); return; }
                }
                stations.add(new ChargingStation(stationId, name, location));
                saveStations();
                refreshStationTable.run();
                stationIdField.setText(""); stationNameField.setText(""); locationField.setText("");
                JOptionPane.showMessageDialog(panel, "Charging station added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteStationButton.addActionListener(e -> {
            int selectedRow = stationTable.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(panel, "Please select a charging station first.", "Selection Required", JOptionPane.WARNING_MESSAGE); return; }
            String stationId = stationModel.getValueAt(selectedRow, 0).toString();
            String stationName = stationModel.getValueAt(selectedRow, 1).toString();
            
            int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete the station: '" + stationName + "'?\nThis will permanently remove the station and ALL its charging points.", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean removed = false;
                for (int i = 0; i < stations.size(); i++) {
                    if (stations.get(i).getStationId().equals(stationId)) {
                        stations.remove(i);
                        removed = true;
                        break;
                    }
                }
                if (removed) {
                    saveStations();
                    refreshStationTable.run();
                    JOptionPane.showMessageDialog(panel, "Charging station deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Station not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addPointButton.addActionListener(e -> {
            try {
                int selectedRow = stationTable.getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(panel, "Please select a charging station first."); return; }
                String stationId = stationModel.getValueAt(selectedRow, 0).toString();
                String pointId = pointIdField.getText().trim();
                String connector = connectorField.getText().trim();
                if (pointId.isEmpty() || connector.isEmpty()) { JOptionPane.showMessageDialog(panel, "Please enter Point ID and Connector Type."); return; }
                
                ChargingStation selectedStation = null;
                for (ChargingStation station : stations) {
                    if (station.getStationId().equals(stationId)) { selectedStation = station; break; }
                }
                if (selectedStation == null) { JOptionPane.showMessageDialog(panel, "Selected station not found."); return; }
                if (selectedStation.findChargingPoint(pointId) != null) { JOptionPane.showMessageDialog(panel, "This charging point ID already exists."); return; }
                
                selectedStation.addChargingPoint(new ChargingPoint(pointId, connector));
                saveStations();
                refreshStationTable.run();
                refreshPointTable.run();
                pointIdField.setText(""); connectorField.setText("");
                JOptionPane.showMessageDialog(panel, "Charging point added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deletePointButton.addActionListener(e -> {
            int selectedStationRow = stationTable.getSelectedRow();
            int selectedPointRow = pointTable.getSelectedRow();
            if (selectedStationRow == -1 || selectedPointRow == -1) { JOptionPane.showMessageDialog(panel, "Please select a station and a charging point first.", "Selection Required", JOptionPane.WARNING_MESSAGE); return; }
            
            String stationId = stationModel.getValueAt(selectedStationRow, 0).toString();
            String pointId = pointModel.getValueAt(selectedPointRow, 0).toString();
            
            int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete charging point: '" + pointId + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean removed = false;
                for (ChargingStation station : stations) {
                    if (station.getStationId().equals(stationId)) {
                        List<ChargingPoint> points = station.getChargingPoints();
                        for (int i = 0; i < points.size(); i++) {
                            if (points.get(i).getPointId().equals(pointId)) {
                                points.remove(i);
                                removed = true;
                                break;
                            }
                        }
                        break;
                    }
                }
                if (removed) {
                    saveStations();
                    refreshStationTable.run();
                    refreshPointTable.run();
                    JOptionPane.showMessageDialog(panel, "Charging point deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Charging point not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(e -> { refreshStationTable.run(); refreshPointTable.run(); });

        JPanel stationInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stationInputPanel.setBorder(BorderFactory.createTitledBorder("Add / Manage Charging Station"));
        stationInputPanel.add(new JLabel("Station ID:"));
        stationInputPanel.add(stationIdField);
        stationInputPanel.add(new JLabel("Name:"));
        stationInputPanel.add(stationNameField);
        stationInputPanel.add(new JLabel("Location:"));
        stationInputPanel.add(locationField);
        stationInputPanel.add(addStationButton);
        stationInputPanel.add(deleteStationButton);
        stationInputPanel.add(refreshButton);

        JPanel pointInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pointInputPanel.setBorder(BorderFactory.createTitledBorder("Add / Manage Charging Point"));
        pointInputPanel.add(new JLabel("Point ID:"));
        pointInputPanel.add(pointIdField);
        pointInputPanel.add(new JLabel("Connector Type:"));
        pointInputPanel.add(connectorField);
        pointInputPanel.add(addPointButton);
        pointInputPanel.add(deletePointButton);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(stationScroll);
        centerPanel.add(pointScroll);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.add(stationInputPanel);
        bottomPanel.add(pointInputPanel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        refreshStationTable.run();
        return panel;
    }

    private JPanel createChargingScreen() {
        return createManagementScreen("Charging Operations", new String[]{"Session ID", "Vehicle ID", "Point ID", "Status"});
    }

    private JPanel createReportScreen() {
        JPanel panel = new JPanel(new BorderLayout(15, 20));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        JLabel title = new JLabel("Reports");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(new Color(35, 47, 62));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        selectionPanel.setBackground(BACKGROUND);
        JLabel stationLabel = new JLabel("Select Charging Station:");
        JComboBox<String> stationComboBox = new JComboBox<>();
        stationComboBox.setPreferredSize(new Dimension(260, 30));
        JButton refreshButton = new JButton("Refresh Stations");
        JButton generateButton = new JButton("Generate Report");
        JButton saveButton = new JButton("Save Report");
        saveButton.setEnabled(false);

        selectionPanel.add(stationLabel);
        selectionPanel.add(stationComboBox);
        selectionPanel.add(refreshButton);
        selectionPanel.add(generateButton);
        selectionPanel.add(saveButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(selectionPanel, BorderLayout.SOUTH);

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setLineWrap(false);
        reportArea.setText("Select a charging station and click Generate Report.");
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        reportScrollPane.setBorder(BorderFactory.createTitledBorder("Generated Report Preview"));

        final String[] generatedReport = {null};
        Runnable clearGeneratedReport = () -> {
            generatedReport[0] = null;
            saveButton.setEnabled(false);
            reportArea.setText("Select a charging station and click Generate Report.");
        };

        Runnable refreshStationOptions = () -> {
            stationComboBox.removeAllItems();
            clearGeneratedReport.run();
            for (ChargingStation station : stations) {
                stationComboBox.addItem(station.getStationId() + " - " + station.getName());
            }
            if (stations.isEmpty()) {
                reportArea.setText("No charging stations found.\nPlease add a station in the Charging Stations screen, then return here and click Refresh Stations.");
            }
        };

        stationComboBox.addActionListener(e -> clearGeneratedReport.run());
        refreshButton.addActionListener(e -> refreshStationOptions.run());

        generateButton.addActionListener(e -> {
            int selectedIndex = stationComboBox.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= stations.size()) {
                JOptionPane.showMessageDialog(this, "Please add/select a charging station first.", "No Station Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                ChargingStation selectedStation = stations.get(selectedIndex);
                generatedReport[0] = ReportGenerator.generateStationPerformanceReport(selectedStation);
                if (generatedReport[0] == null || generatedReport[0].trim().isEmpty()) {
                    generatedReport[0] = null;
                    saveButton.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "The report could not be generated.", "Report Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                reportArea.setText(generatedReport[0]);
                reportArea.setCaretPosition(0);
                saveButton.setEnabled(true);
                System.out.println("Station report generated for: " + selectedStation.getName());
            } catch (Exception ex) {
                generatedReport[0] = null;
                saveButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        saveButton.addActionListener(e -> {
            if (generatedReport[0] == null || generatedReport[0].trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please generate a report before saving.", "No Report", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Charging Station Report");
            fileChooser.setSelectedFile(new File("ChargingStationReport.txt"));
            int choice = fileChooser.showSaveDialog(this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    boolean saved = ReportSaver.saveReport(selectedFile.getAbsolutePath(), generatedReport[0]);
                    if (saved) {
                        JOptionPane.showMessageDialog(this, "Report saved successfully to:\n" + selectedFile.getAbsolutePath(), "Report Saved", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Could not save the report. Check the console for details.", "Save Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving report: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(reportScrollPane, BorderLayout.CENTER);
        refreshStationOptions.run();
        return panel;
    }

    private JPanel createNetworkScreen() {
        JPanel panel = createBaseScreen("Network Monitoring");
        JPanel center = new JPanel();
        center.setBackground(BACKGROUND);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        JLabel description = new JLabel("Manage communication with the charging station server");
        description.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton startServer = new JButton("Start Server");
        JButton connectClient = new JButton("Connect Client");
        center.add(description);
        center.add(Box.createVerticalStrut(25));
        center.add(startServer);
        center.add(Box.createVerticalStrut(12));
        center.add(connectClient);
        startServer.addActionListener(e -> JOptionPane.showMessageDialog(this, "Server connection will be integrated with ChargingStationServer."));
        connectClient.addActionListener(e -> JOptionPane.showMessageDialog(this, "Client connection will be integrated with ChargingStationClient."));
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createManagementScreen(String heading, String[] columns) {
        JPanel panel = createBaseScreen(heading);
        JPanel center = new JPanel(new BorderLayout(10, 15));
        center.setBackground(BACKGROUND);
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND);
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        addButton.addActionListener(e -> {
            JTextField[] fields = new JTextField[columns.length];
            JPanel form = new JPanel(new GridLayout(columns.length, 2, 8, 8));
            for (int i = 0; i < columns.length; i++) {
                form.add(new JLabel(columns[i] + ":"));
                fields[i] = new JTextField(15);
                form.add(fields[i]);
            }
            int result = JOptionPane.showConfirmDialog(this, form, "Enter " + heading + " Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Object[] row = new Object[columns.length];
                boolean valid = true;
                for (int i = 0; i < columns.length; i++) {
                    row[i] = fields[i].getText().trim();
                    if (row[i].toString().isEmpty()) { valid = false; break; }
                }
                if (valid) model.addRow(row);
                else JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) model.removeRow(selectedRow);
            else JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        });

        clearButton.addActionListener(e -> model.setRowCount(0));
        center.add(buttonPanel, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }
}
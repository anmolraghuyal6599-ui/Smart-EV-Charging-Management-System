# ⚡ Smart EV Charging Station Management System

### Java-Based Object-Oriented Application

## 📌 About the Project

The **Smart EV Charging Station Management System** is a Java-based application designed to manage and organize electric vehicle charging station operations.

With the increasing adoption of electric vehicles, efficiently managing charging stations, vehicle information, charging-point availability, and transactions has become important.

This project provides a structured solution for managing EV owners, vehicles, charging stations, charging sessions, and billing operations. It also incorporates multithreading, file handling, exception handling, and client-server communication.

## 🎯 Objectives

- Manage EV owners and their vehicle information.
- Maintain charging station and charging-point details.
- Monitor charging-point availability and charging sessions.
- Prevent double booking of charging points.
- Manage wallet balances and charging transactions.
- Support multiple charging sessions using multithreading.
- Generate and store charging receipts.
- Enable communication between charging stations and a central server.

## ⚙️ Key Features

- **User and Vehicle Management:** Maintain owner profiles and associated vehicle information.
- **Charging Station Management:** Manage charging points and their availability.
- **Charging Session Management:** Initiate, monitor, and complete charging sessions.
- **Wallet and Billing:** Process charging fees and maintain transaction records.
- **Exception Handling:** Handle invalid operations, unavailable ports, and insufficient balances.
- **Multithreading:** Execute charging sessions concurrently.
- **File Handling:** Store receipts, generate reports, and create backups.
- **Socket Programming:** Enable TCP-based communication between the client and central server.

## 🛠️ Technologies Used

- **Programming Language:** Java
- **Core Concepts:** Object-Oriented Programming, Inheritance, Polymorphism, Abstraction and Encapsulation
- **Advanced Java:** Packages, Exception Handling, Multithreading and Synchronization
- **File Handling:** Character Streams and Byte Streams
- **Networking:** TCP Socket Programming
- **Development Environment:** Visual Studio Code
- **Version Control:** Git and GitHub

## 📂 Project Architecture

| Package | Responsibility |
|---|---|
| `model` | User, vehicle, station and session entities |
| `service` | Charging operations and business logic |
| `exception` | Custom exception handling |
| `util` | Reports, logs and file operations |
| `network` | Client-server socket communication |

## 🔄 System Workflow

1. Register or select an EV owner and associated vehicle.
2. View available charging stations and charging points.
3. Select an available charging point.
4. Validate the selection and initiate a charging session.
5. Monitor charging progress through background threads.
6. Process charging fees and update wallet balances.
7. Generate and save transaction receipts.
8. Release the charging point after session completion.

## 🧪 Testing

The implementation includes testing of:

- Owner and vehicle registration.
- Charging station and port availability.
- Charging session execution.
- Double-booking prevention.
- Receipt generation and file persistence.
- TCP client-server communication.

## 🌱 Sustainable Development Goal

**SDG 7 – Affordable and Clean Energy**

The project supports the broader adoption of electric mobility by providing a software-based solution for organized and efficient EV charging station management.

## 🚀 Future Scope

- Graphical user interface integration.
- Database connectivity using JDBC and MySQL.
- Enhanced charging analytics and reporting.
- Improved station monitoring and management.
- Integration with physical EV charging infrastructure.


package model;

// Admin inherits from Account
public class Admin extends Account {

    // Admin's responsibility or position
    private String role;

    // Constructor
    public Admin(String adminId, String name, String contact, String role) {
        super(adminId, name, contact);

        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }

        this.role = role;
    }

    // Getter
    public String getRole() {
        return role;
    }

    // Setter
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
        this.role = role;
    }

    // Implement abstract method from Account
    @Override
    public void displayRole() {
        System.out.println("Role: " + role);
    }

    // Display admin details
    public void displayAdminDetails() {
        displayAccountDetails();
        System.out.println("Admin Role: " + role);
    }
}

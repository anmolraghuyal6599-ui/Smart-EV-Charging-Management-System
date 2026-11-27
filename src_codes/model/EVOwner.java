
package model;

// EVOwner inherits from User
public class EVOwner extends User {

    private double walletBalance;

    // Constructor
    public EVOwner(String userId, String name, String contact,
                   double walletBalance) {

        super(userId, name, contact);

        if (walletBalance < 0) {
            throw new IllegalArgumentException("Wallet balance cannot be negative.");
        }

        this.walletBalance = walletBalance;
    }

    // Getter
    public double getWalletBalance() {
        return walletBalance;
    }

    // Add money to wallet
    public void addBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        walletBalance += amount;
    }

    // Deduct money from wallet
    public void deductBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        if (amount > walletBalance) {
            throw new IllegalArgumentException("Insufficient wallet balance.");
        }

        walletBalance -= amount;
    }

    // Display EV owner details
    public void displayOwnerDetails() {
        displayUserDetails();
        System.out.println("Wallet Balance: Rs. " + walletBalance);
    }

    // Override displayRole()
    @Override
    public void displayRole() {
        System.out.println("Role: EV Owner");
    }
}

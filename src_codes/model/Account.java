
package model;

import java.io.Serializable;

public abstract class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    // Encapsulation
    private final String accountId;
    private String name;
    private String contact;

    // Constructor
    public Account(String accountId, String name, String contact) {

        this.accountId = accountId;

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact cannot be empty");
        }

        this.name = name;
        this.contact = contact;
    }

    // Getters
    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public void setContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact cannot be empty.");
        }
        this.contact = contact;
    }

    // Abstraction
    public abstract void displayRole();

    // Display account details
    public void displayAccountDetails() {
        System.out.println("Account ID: " + accountId);
        System.out.println("Name: " + name);
        System.out.println("Contact: " + contact);
    }

    // Convert account details into String
    @Override
    public String toString() {
        return "Account ID: " + accountId
                + ", Name: " + name
                + ", Contact: " + contact;
    }
}

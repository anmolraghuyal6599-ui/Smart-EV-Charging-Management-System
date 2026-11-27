package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Account;
import model.Admin;
import model.EVOwner;
import model.User;
import storage.DataStore;
import storage.FileDataStore;

public class UserService {

    private final List<Account> accounts;
    private final DataStore<Account> accountStore;

    public UserService() {
        accounts = new ArrayList<>();
        accountStore = new FileDataStore<>("data/accounts.dat");

        try {
            accounts.addAll(accountStore.load());
            System.out.println("Accounts loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to load saved accounts.", e);
        }
    }

    private void saveAccounts() {
        try {
            accountStore.save(accounts);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save account data.", e);
        }
    }

    public void saveChanges() {
        saveAccounts();
        System.out.println("Account changes saved successfully.");
    }

    public void registerAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        if (findAccountById(account.getAccountId()) != null) {
            throw new IllegalArgumentException("Account ID already exists.");
        }

        accounts.add(account);
        try {
            saveAccounts();
            System.out.println("Account registered successfully.");
        } catch (IllegalStateException e) {
            accounts.remove(account);
            throw e;
        }
    }

    public void registerUser(User user) {
        registerAccount(user);
    }

    public void registerEVOwner(EVOwner owner) {
        registerAccount(owner);
    }

    public void registerAdmin(Admin admin) {
        registerAccount(admin);
    }

    public Account findAccountById(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be empty.");
        }
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

    public boolean accountExists(String accountId) {
        return findAccountById(accountId) != null;
    }

    public boolean removeAccount(String accountId) {
        Account account = findAccountById(accountId);
        if (account == null) {
            return false;
        }

        int index = accounts.indexOf(account);
        accounts.remove(account);

        try {
            saveAccounts();
            return true;
        } catch (IllegalStateException e) {
            accounts.add(index, account);
            throw e;
        }
    }

    public List<Account> getAllAccounts() {
        return Collections.unmodifiableList(new ArrayList<>(accounts));
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts registered.");
            return;
        }
        for (Account account : accounts) {
            account.displayAccountDetails();
            account.displayRole();
            System.out.println("----------------------");
        }
    }
}
package ecommerce.domain.users;

import ecommerce.data.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;            
    private String username;
    private String password;            
    private String email;
    private final LocalDateTime registeredDate;  
    private final String role;          

    
    protected static DataStore dataStore;   

    public static void setDataStore(DataStore ds) {
        dataStore = ds;
    }
    
    
    // constructor 
    public User(String id, String username, String password, String email, String role, LocalDateTime registeredDate) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID cannot be empty");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        if (password == null || password.length() < 4) throw new IllegalArgumentException("Password too short");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        
        this.id = id;
        this.username = username.trim();
        this.password = password; // Store password as-is
        this.email = email.trim();
        this.role = role;

        // If loaded from DB/file → use existing date
        // If newly created → use now()
        this.registeredDate = (registeredDate != null) 
                ? registeredDate 
                : LocalDateTime.now();
    }


    //Getters
    public String getId() {
        return id; 
    }
    public String getUsername() { 
        return username; 
    }
    public String getEmail() {
        return email; 
    }
    public LocalDateTime getRegisteredDate() {
        return registeredDate; 
    }
    public String getRole() {
        return role;
    }
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }
    
    
    // Setters
    public final void setUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");
        this.username = username.trim();
        DataStore.getInstance().saveUsers();
    }

    public final void setPassword(String password) {
        if (password == null || password.length() < 4)
            throw new IllegalArgumentException("Password too short");
        this.password = password;
        DataStore.getInstance().saveUsers();
    }

    public final void setEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email");
        this.email = email.trim();
        DataStore.getInstance().saveUsers();
    }
    

    public boolean wasRegisteredBetween(LocalDateTime start, LocalDateTime end) {
        return !registeredDate.isBefore(start) && !registeredDate.isAfter(end);
    }

    
    public void addUser() {
        if (dataStore == null) throw new IllegalStateException("DataStore not set");
        dataStore.getUsers().put(this.id, this);
        dataStore.saveUsers();
        System.out.println("User saved: " + this.username + " (ID: " + this.id + ", Role: " + this.role + ")");
    }

    public void removeUser() {
        if (dataStore == null) throw new IllegalStateException("DataStore not set");
        dataStore.getUsers().remove(this.id);
        dataStore.saveUsers();
    }
    
    
    //   //////////////////Authentication\\\\\\\\\\\\\\\\\\\\\\
    public static User login(String username, String password) {
        System.out.println("Login attempt - Username: " + username);
        
        for (User user : DataStore.getInstance().getUsers().values()) {
            System.out.println("Checking user: " + user.getUsername() + " (Role: " + user.getRole() + ")");
            
            if (user.getUsername().equals(username)) {
                System.out.println("Username match found!");
                System.out.println("Stored password: [" + user.password + "]");
                System.out.println("Input password: [" + password + "]");
                System.out.println("Passwords match: " + user.checkPassword(password));
                
                if (user.checkPassword(password)) {
                    System.out.println("Login successful for: " + username);
                    return user; // successful login
                }
            }
        }
        
        System.out.println("Login failed for: " + username);
        throw new IllegalArgumentException("Invalid username or password");
    }

    public static boolean isAdmin(User user) {
        return user != null && user.getRole().equals("ADMIN");
    }

    public static boolean isSeller(User user) {
        return user != null && user.getRole().equals("SELLER");
    }

    public static boolean isCustomer(User user) {
        return user != null && user.getRole().equals("CUSTOMER");
    }

    
    @Override
    public String toString() {
        return String.format("%s{id=%s, username=%s, email=%s, role=%s}",
                getClass().getSimpleName(), id, username, email, role);
    }
}
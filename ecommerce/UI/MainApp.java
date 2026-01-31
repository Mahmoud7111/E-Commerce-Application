package ecommerce.UI;

import ecommerce.data.DataStore;
import ecommerce.domain.Shopping.Cart;
import ecommerce.domain.Shopping.Order;
import ecommerce.domain.Shopping.Product;
import ecommerce.domain.users.Admin;
import ecommerce.domain.users.User;
import ecommerce.util.IDGenerator;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println("\n========================================");
        System.out.println("STARTING E-COMMERCE APPLICATION");
        System.out.println("========================================\n");
        
        // Initialize datastore ONCE
        DataStore ds = DataStore.getInstance();
        
        // Give its reference to all domain classes
        User.setDataStore(ds);
        Product.setDataStore(ds);
        Cart.setDataStore(ds);
        Order.setDataStore(ds);
        
        System.out.println("DataStore initialized.");
        System.out.println("Current users in system: " + ds.getUsers().size());
        
        // List all existing users
        if (!ds.getUsers().isEmpty()) {
            System.out.println("\nExisting users:");
            ds.getUsers().values().forEach(user -> 
                System.out.println("  - " + user.getUsername() + " (ID: " + user.getId() + ", Role: " + user.getRole() + ")")
            );
        }
        
        // Create default admin if no admin exists
        createDefaultAdminIfNeeded();
        
        System.out.println("\nTotal users after initialization: " + ds.getUsers().size());
        System.out.println("========================================\n");
        
        LoginMenu.show(stage);
    }
    
    /**
     * Creates a default admin account if no admin exists in the system
     */
    private void createDefaultAdminIfNeeded() {
        DataStore ds = DataStore.getInstance();
        
        // Check if any admin exists
        boolean adminExists = ds.getUsers().values().stream()
            .anyMatch(user -> "ADMIN".equals(user.getRole()));
        
        if (!adminExists) {
            System.out.println("\n===========================================");
            System.out.println("⚠️  NO ADMIN FOUND - Creating default admin");
            System.out.println("===========================================");
            
            String adminId = "U1000"; // Fixed ID for admin
            
            try {
                Admin defaultAdmin = new Admin(
                    adminId,
                    "admin",
                    "admin",
                    "admin@ecommerce.com",
                    LocalDateTime.now()
                );
                
                // Add and save
                defaultAdmin.addUser();
                
                // Force save to ensure it persists
                ds.saveUsers();
                
                // Verify it was added
                User savedAdmin = ds.getUsers().get(adminId);
                
                if (savedAdmin != null) {
                    System.out.println("\n✅ DEFAULT ADMIN CREATED SUCCESSFULLY!");
                    System.out.println("   ID: " + adminId);
                    System.out.println("   Username: admin");
                    System.out.println("   Password: admin");
                    System.out.println("   Email: admin@ecommerce.com");
                    System.out.println("   Role: " + savedAdmin.getRole());
                    System.out.println("===========================================\n");
                } else {
                    System.err.println("\n❌ ERROR: Admin was not saved properly!");
                    System.err.println("===========================================\n");
                }
                
            } catch (Exception e) {
                System.err.println("\n❌ ERROR creating admin: " + e.getMessage());
            }
        } else {
            System.out.println("✓ Admin account already exists in the system.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
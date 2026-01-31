package ecommerce.util;

import ecommerce.data.DataStore;
import ecommerce.domain.Shopping.Order;
import ecommerce.domain.Shopping.Product;
import ecommerce.domain.users.User;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates short, human-readable IDs for the system
 * IDs are persistent and won't collide even after application restarts
 */
public class IDGenerator {
    
    private static final String COUNTER_FILE = "dataFolder" + File.separator + "id_counters.dat";
    
    private static AtomicInteger userCounter;
    private static AtomicInteger productCounter;
    private static AtomicInteger orderCounter;
    
    private static boolean initialized = false;
    
    /**
     * Initialize counters by scanning existing data
     */
    public static synchronized void initialize(DataStore dataStore) {
        if (initialized) {
            return;
        }
        
        System.out.println("\n=== Initializing ID Generator ===");
        
        // Try to load saved counters first
        if (loadCounters()) {
            System.out.println("✓ Loaded counters from file");
        } else {
            System.out.println("No saved counters found, scanning existing data...");
            
            // Scan existing data to find highest IDs
            int maxUserId = scanMaxUserId(dataStore);
            int maxProductId = scanMaxProductId(dataStore);
            int maxOrderId = scanMaxOrderId(dataStore);
            
            // Start from max + 1, or 1000 if no data exists
            userCounter = new AtomicInteger(Math.max(maxUserId + 1, 1000));
            productCounter = new AtomicInteger(Math.max(maxProductId + 1, 1000));
            orderCounter = new AtomicInteger(Math.max(maxOrderId + 1, 1000));
            
            System.out.println("Initialized counters from existing data:");
            System.out.println("  User counter: " + userCounter.get());
            System.out.println("  Product counter: " + productCounter.get());
            System.out.println("  Order counter: " + orderCounter.get());
            
            // Save the initialized counters
            saveCounters();
        }
        
        initialized = true;
        System.out.println("=================================\n");
    }
    
    /**
     * Scan existing users to find the highest ID number
     */
    private static int scanMaxUserId(DataStore dataStore) {
        int max = 999;
        for (User user : dataStore.getUsers().values()) {
            String id = user.getId();
            if (id.startsWith("U")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip non-numeric IDs
                }
            }
        }
        System.out.println("  Max User ID found: U" + max);
        return max;
    }
    
    /**
     * Scan existing products to find the highest ID number
     */
    private static int scanMaxProductId(DataStore dataStore) {
        int max = 999;
        for (Product product : dataStore.getProducts().values()) {
            String id = product.getProductId();
            if (id.startsWith("P")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip non-numeric IDs
                }
            }
        }
        System.out.println("  Max Product ID found: P" + max);
        return max;
    }
    
    /**
     * Scan existing orders to find the highest ID number
     */
    private static int scanMaxOrderId(DataStore dataStore) {
        int max = 999;
        for (Order order : dataStore.getOrders().values()) {
            String id = order.getOrderId();
            if (id.startsWith("ORD")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip non-numeric IDs
                }
            }
        }
        System.out.println("  Max Order ID found: ORD" + max);
        return max;
    }
    
    /**
     * Save counters to file
     */
    private static void saveCounters() {
        try {
            // Ensure directory exists
            File file = new File(COUNTER_FILE);
            file.getParentFile().mkdirs();
            
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                dos.writeInt(userCounter.get());
                dos.writeInt(productCounter.get());
                dos.writeInt(orderCounter.get());
            }
            System.out.println("✓ Counters saved to file");
        } catch (IOException e) {
            System.err.println("Warning: Could not save ID counters: " + e.getMessage());
        }
    }
    
    /**
     * Load counters from file
     */
    private static boolean loadCounters() {
        File file = new File(COUNTER_FILE);
        if (!file.exists()) {
            return false;
        }
        
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            userCounter = new AtomicInteger(dis.readInt());
            productCounter = new AtomicInteger(dis.readInt());
            orderCounter = new AtomicInteger(dis.readInt());
            
            System.out.println("Loaded counters:");
            System.out.println("  User counter: " + userCounter.get());
            System.out.println("  Product counter: " + productCounter.get());
            System.out.println("  Order counter: " + orderCounter.get());
            
            return true;
        } catch (IOException e) {
            System.err.println("Warning: Could not load ID counters: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate a user ID (U1001, U1002, etc.)
     */
    public static String generateUserId() {
        if (!initialized) {
            throw new IllegalStateException("IDGenerator not initialized. Call initialize() first!");
        }
        String id = "U" + userCounter.getAndIncrement();
        saveCounters(); // Save after each generation
        System.out.println("Generated User ID: " + id);
        return id;
    }
    
    /**
     * Generate a product ID (P1001, P1002, etc.)
     */
    public static String generateProductId() {
        if (!initialized) {
            throw new IllegalStateException("IDGenerator not initialized. Call initialize() first!");
        }
        String id = "P" + productCounter.getAndIncrement();
        saveCounters(); // Save after each generation
        System.out.println("Generated Product ID: " + id);
        return id;
    }
    
    /**
     * Generate an order ID (ORD1001, ORD1002, etc.)
     */
    public static String generateOrderId() {
        if (!initialized) {
            throw new IllegalStateException("IDGenerator not initialized. Call initialize() first!");
        }
        String id = "ORD" + orderCounter.getAndIncrement();
        saveCounters(); // Save after each generation
        System.out.println("Generated Order ID: " + id);
        return id;
    }
    
    /**
     * Generate a timestamped order ID (for uniqueness)
     */
    public static String generateTimestampedOrderId() {
        return "ORD" + System.currentTimeMillis();
    }
    
    /**
     * Get current counter values (for debugging)
     */
    public static void printStatus() {
        System.out.println("\n=== ID Generator Status ===");
        System.out.println("Initialized: " + initialized);
        if (initialized) {
            System.out.println("Next User ID: U" + userCounter.get());
            System.out.println("Next Product ID: P" + productCounter.get());
            System.out.println("Next Order ID: ORD" + orderCounter.get());
        }
        System.out.println("===========================\n");
    }
    
    /**
     * Reset counters (useful for testing - BE CAREFUL!)
     */
    public static void reset() {
        userCounter = new AtomicInteger(1000);
        productCounter = new AtomicInteger(1000);
        orderCounter = new AtomicInteger(1000);
        initialized = true;
        saveCounters();
        System.out.println("⚠️ ID Generator counters have been reset!");
    }
}
package ecommerce.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates short, human-readable IDs for the system
 */
public class IDGenerator {
    
    private static final AtomicInteger userCounter = new AtomicInteger(1000);
    private static final AtomicInteger productCounter = new AtomicInteger(1000);
    private static final AtomicInteger orderCounter = new AtomicInteger(1000);
    
    /**
     * Generate a user ID (U1001, U1002, etc.)
     * @return 
     */
    public static String generateUserId() {
        return "U" + userCounter.getAndIncrement();
    }
    
    /**
     * Generate a product ID (P1001, P1002, etc.)
     * @return 
     */
    public static String generateProductId() {
        return "P" + productCounter.getAndIncrement();
    }
    
    /**
     * Generate an order ID (ORD1001, ORD1002, etc.)
     * @return 
     */
    public static String generateOrderId() {
        return "ORD" + orderCounter.getAndIncrement();
    }
    
    /**
     * Generate a timestamped order ID
     * @return 
     */
    public static String generateTimestampedOrderId() {
        return "ORD" + System.currentTimeMillis();
    }
    
    /**
     * Reset counters (useful for testing)
     */
    public static void reset() {
        userCounter.set(1000);
        productCounter.set(1000);
        orderCounter.set(1000);
    }
}
import java.io.*;
import java.util.*;

// Main Class
public class BookMyStayApp {

    // Reservation Entity (Serializable)
    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;

        private String reservationId;
        private String guestName;
        private String roomType;
        private double price;

        public Reservation(String reservationId, String guestName, String roomType, double price) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.price = price;
        }

        public String getReservationId() { return reservationId; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public double getPrice() { return price; }
    }

    // System State (Serializable)
    static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L;

        List<Reservation> reservations;
        Map<String, Integer> inventory;

        public SystemState(List<Reservation> reservations, Map<String, Integer> inventory) {
            this.reservations = reservations;
            this.inventory = inventory;
        }
    }

    // Persistence Service
    static class PersistenceService {
        private static final String FILE_NAME = "booking_data.ser";

        // Save state to file
        public static void save(SystemState state) {
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(state);
                System.out.println("System state saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }

        // Load state from file
        public static SystemState load() {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                System.out.println("System state loaded successfully.");
                return (SystemState) ois.readObject();

            } catch (FileNotFoundException e) {
                System.out.println("No previous data found. Starting fresh.");
            } catch (Exception e) {
                System.out.println("Error loading data. Starting with safe defaults.");
            }

            // Safe fallback state
            return new SystemState(new ArrayList<>(), getDefaultInventory());
        }

        private static Map<String, Integer> getDefaultInventory() {
            Map<String, Integer> inventory = new HashMap<>();
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
            return inventory;
        }
    }

    // Main Flow
    public static void main(String[] args) {

        // Step 1: Load previous state
        SystemState state = PersistenceService.load();

        List<Reservation> reservations = state.reservations;
        Map<String, Integer> inventory = state.inventory;

        // Step 2: Simulate booking
        if (inventory.get("Deluxe") > 0) {
            Reservation r = new Reservation("R001", "Ram", "Deluxe", 3000);
            reservations.add(r);
            inventory.put("Deluxe", inventory.get("Deluxe") - 1);

            System.out.println("Booking added: " + r.getReservationId());
        } else {
            System.out.println("No Deluxe rooms available.");
        }

        // Step 3: Display current state
        System.out.println("\nCurrent Bookings:");
        for (Reservation r : reservations) {
            System.out.println(r.getReservationId() + " | " +
                    r.getGuestName() + " | " +
                    r.getRoomType());
        }

        System.out.println("\nCurrent Inventory: " + inventory);

        // Step 4: Save state before shutdown
        PersistenceService.save(new SystemState(reservations, inventory));
    }
}
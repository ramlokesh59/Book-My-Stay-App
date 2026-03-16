import java.util.HashMap;
import java.util.Map;


public class BookMyStayApp {

    public static void main(String[] args) {

        // Centralized inventory using HashMap
        Map<String, Integer> inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);

        // Room catalog with details
        Map<String, String> roomDetails = new HashMap<>();

        roomDetails.put("Single Room", "Beds: 1 | Size: 200 sq.ft | Price: ₹2500");
        roomDetails.put("Double Room", "Beds: 2 | Size: 350 sq.ft | Price: ₹4000");
        roomDetails.put("Suite Room", "Beds: 3 | Size: 600 sq.ft | Price: ₹8500");

        System.out.println("----- AVAILABLE ROOMS -----");

        // Guest searches available rooms
        for (String roomType : inventory.keySet()) {

            int available = inventory.get(roomType);

            // Show only available rooms
            if (available > 0) {

                System.out.println("Room Type: " + roomType);
                System.out.println(roomDetails.get(roomType));
                System.out.println("Available: " + available);
                System.out.println();
            }
        }

        System.out.println("Search completed. Inventory not modified.");
    }
}
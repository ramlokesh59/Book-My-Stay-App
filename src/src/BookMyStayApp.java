import java.util.*;

/**
 * BookMyStayApp
 *
 * Demonstrates reservation confirmation and safe room allocation.
 * Booking requests are processed in FIFO order and rooms are assigned
 * unique IDs while maintaining inventory consistency.
 *
 * @author Ram Lokesh
 * @version 1.0
 */
public class BookMyStayApp {

    /**
     * Reservation represents a booking request from a guest
     */
    static class Reservation {

        String guestName;
        String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    public static void main(String[] args) {

        // Inventory (room type -> available count)
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);

        // Booking request queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Arun", "Single Room"));
        bookingQueue.add(new Reservation("Meena", "Double Room"));
        bookingQueue.add(new Reservation("Rahul", "Suite Room"));
        bookingQueue.add(new Reservation("Priya", "Single Room"));

        // Set to store all allocated room IDs (ensures uniqueness)
        Set<String> allocatedRoomIds = new HashSet<>();

        // Map to track room type -> allocated room IDs
        Map<String, Set<String>> allocatedRooms = new HashMap<>();

        System.out.println("----- PROCESSING RESERVATIONS -----");

        int roomCounter = 1;

        // Process booking requests in FIFO order
        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.poll();

            int available = inventory.getOrDefault(request.roomType, 0);

            if (available > 0) {

                // Generate unique room ID
                String roomId = request.roomType.replace(" ", "") + "-" + roomCounter++;

                // Ensure uniqueness using Set
                allocatedRoomIds.add(roomId);

                // Track allocated room IDs by type
                allocatedRooms
                        .computeIfAbsent(request.roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.put(request.roomType, available - 1);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + request.guestName);
                System.out.println("Room Type: " + request.roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();

            } else {

                System.out.println("Reservation Failed for " + request.guestName +
                        " (No " + request.roomType + " available)\n");
            }
        }

        System.out.println("----- FINAL INVENTORY -----");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Available: " + inventory.get(type));
        }
    }
}
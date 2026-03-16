import java.util.LinkedList;
import java.util.Queue;


public class BookMyStayApp {

    /**
     * Reservation class representing a guest booking request
     */
    static class Reservation {

        String guestName;
        String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public void displayReservation() {
            System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
        }
    }

    public static void main(String[] args) {

        // Booking request queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guests submit booking requests
        bookingQueue.add(new Reservation("Arun", "Single Room"));
        bookingQueue.add(new Reservation("Meena", "Double Room"));
        bookingQueue.add(new Reservation("Rahul", "Suite Room"));
        bookingQueue.add(new Reservation("Priya", "Single Room"));

        System.out.println("----- BOOKING REQUEST QUEUE -----");

        // Display queued booking requests in arrival order
        for (Reservation reservation : bookingQueue) {
            reservation.displayReservation();
        }

        System.out.println("\nRequests stored in FIFO order.");
        System.out.println("No inventory updates performed at this stage.");
    }
}
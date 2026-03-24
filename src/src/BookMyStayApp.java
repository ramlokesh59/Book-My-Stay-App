import java.util.*;

// Main Class
public class BookMyStayApp {

    // Reservation Entity
    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;
        private String roomId;
        private double price;
        private boolean isCancelled;

        public Reservation(String reservationId, String guestName, String roomType, String roomId, double price) {
            this.reservationId = reservationId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.roomId = roomId;
            this.price = price;
            this.isCancelled = false;
        }

        public String getReservationId() { return reservationId; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public String getRoomId() { return roomId; }
        public double getPrice() { return price; }
        public boolean isCancelled() { return isCancelled; }

        public void cancel() { this.isCancelled = true; }
    }

    // Booking History
    static class BookingHistory {
        private List<Reservation> reservations = new ArrayList<>();

        public void addReservation(Reservation r) {
            reservations.add(r);
        }

        public Reservation findReservation(String id) {
            for (Reservation r : reservations) {
                if (r.getReservationId().equals(id)) {
                    return r;
                }
            }
            return null;
        }

        public List<Reservation> getAllReservations() {
            return Collections.unmodifiableList(reservations);
        }
    }

    // Booking Service
    static class BookingService {
        private Map<String, Integer> inventory = new HashMap<>();
        private BookingHistory history;
        private int roomCounter = 1;

        public BookingService(BookingHistory history) {
            this.history = history;

            inventory.put("Standard", 2);
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
        }

        public void bookRoom(String id, String guest, String type, double price) {
            if (!inventory.containsKey(type) || inventory.get(type) <= 0) {
                System.out.println("Booking failed: No availability for " + type);
                return;
            }

            String roomId = type.substring(0, 1).toUpperCase() + roomCounter++;
            inventory.put(type, inventory.get(type) - 1);

            Reservation r = new Reservation(id, guest, type, roomId, price);
            history.addReservation(r);

            System.out.println("Booking confirmed: " + id + " | Room ID: " + roomId);
        }

        public Map<String, Integer> getInventory() {
            return inventory;
        }
    }

    // Cancellation Service (Rollback using Stack)
    static class CancellationService {
        private Stack<String> rollbackStack = new Stack<>();
        private BookingHistory history;
        private Map<String, Integer> inventory;

        public CancellationService(BookingHistory history, Map<String, Integer> inventory) {
            this.history = history;
            this.inventory = inventory;
        }

        public void cancelBooking(String reservationId) {

            Reservation r = history.findReservation(reservationId);

            // Validate existence
            if (r == null) {
                System.out.println("Cancellation failed: Reservation not found");
                return;
            }

            // Prevent duplicate cancellation
            if (r.isCancelled()) {
                System.out.println("Cancellation failed: Already cancelled");
                return;
            }

            // Step 1: Push roomId to rollback stack
            rollbackStack.push(r.getRoomId());

            // Step 2: Restore inventory
            String type = r.getRoomType();
            inventory.put(type, inventory.get(type) + 1);

            // Step 3: Mark as cancelled
            r.cancel();

            System.out.println("Cancellation successful for " + reservationId +
                    " | Released Room: " + rollbackStack.peek());
        }
    }

    // Report Service
    static class ReportService {
        public void showAllBookings(List<Reservation> list) {
            System.out.println("\n===== Booking Status =====");

            for (Reservation r : list) {
                System.out.println(
                        r.getReservationId() + " | " +
                                r.getGuestName() + " | " +
                                r.getRoomType() + " | " +
                                r.getRoomId() + " | " +
                                (r.isCancelled() ? "CANCELLED" : "ACTIVE")
                );
            }
        }
    }

    // Main Flow
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(history);
        CancellationService cancelService =
                new CancellationService(history, bookingService.getInventory());
        ReportService reportService = new ReportService();

        // Book rooms
        bookingService.bookRoom("R001", "Ram", "Deluxe", 3000);
        bookingService.bookRoom("R002", "Lokesh", "Suite", 5000);

        // Cancel booking
        cancelService.cancelBooking("R001");

        // Invalid cancellation
        cancelService.cancelBooking("R003"); // not exist

        // Duplicate cancellation
        cancelService.cancelBooking("R001");

        // Final report
        reportService.showAllBookings(history.getAllReservations());
    }
}
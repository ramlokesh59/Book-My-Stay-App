import java.util.*;

// Main Class
public class BookMyStayApp {

    // Custom Exception
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    // Reservation Entity
    static class Reservation {
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

    // Booking History
    static class BookingHistory {
        private List<Reservation> reservations = new ArrayList<>();

        public void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }

        public List<Reservation> getAllReservations() {
            return Collections.unmodifiableList(reservations);
        }
    }

    // Validator
    static class BookingValidator {

        private static final List<String> VALID_ROOM_TYPES =
                Arrays.asList("Standard", "Deluxe", "Suite");

        public static void validate(String roomType, int availableRooms)
                throws InvalidBookingException {

            // Validate room type
            if (!VALID_ROOM_TYPES.contains(roomType)) {
                throw new InvalidBookingException("Invalid room type: " + roomType);
            }

            // Validate availability
            if (availableRooms <= 0) {
                throw new InvalidBookingException("No rooms available for type: " + roomType);
            }
        }
    }

    // Booking Service
    static class BookingService {
        private Map<String, Integer> inventory = new HashMap<>();
        private BookingHistory history;

        public BookingService(BookingHistory history) {
            this.history = history;

            // Initial inventory
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
        }

        public void bookRoom(String id, String guest, String roomType, double price) {
            try {
                // Step 1: Validate
                BookingValidator.validate(roomType, inventory.getOrDefault(roomType, 0));

                // Step 2: Update inventory
                inventory.put(roomType, inventory.get(roomType) - 1);

                // Step 3: Store booking
                Reservation r = new Reservation(id, guest, roomType, price);
                history.addReservation(r);

                System.out.println("Booking successful for " + guest + " (" + roomType + ")");

            } catch (InvalidBookingException e) {
                // Graceful failure
                System.out.println("Booking failed: " + e.getMessage());
            }
        }
    }

    // Report Service
    static class BookingReportService {
        public void generateReport(List<Reservation> reservations) {
            System.out.println("\n===== Booking Report =====");

            double total = 0;
            for (Reservation r : reservations) {
                total += r.getPrice();
            }

            System.out.println("Total Bookings: " + reservations.size());
            System.out.println("Total Revenue: ₹" + total);

            for (Reservation r : reservations) {
                System.out.println(
                        r.getReservationId() + " | " +
                                r.getGuestName() + " | " +
                                r.getRoomType() + " | ₹" +
                                r.getPrice()
                );
            }
        }
    }

    // Main Flow
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(history);
        BookingReportService reportService = new BookingReportService();

        // Valid bookings
        bookingService.bookRoom("R001", "Ram", "Deluxe", 3000);
        bookingService.bookRoom("R002", "Lokesh", "Suite", 5000);

        // Invalid booking (wrong room type)
        bookingService.bookRoom("R003", "Arun", "Luxury", 7000);

        // Invalid booking (no availability)
        bookingService.bookRoom("R004", "Kumar", "Suite", 5000);

        // Generate report
        reportService.generateReport(history.getAllReservations());
    }
}
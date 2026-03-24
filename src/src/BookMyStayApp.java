import java.util.*;

// Main Class
public class BookMyStayApp {

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

    // Booking History (Storage)
    static class BookingHistory {
        private List<Reservation> reservations = new ArrayList<>();

        public void addReservation(Reservation reservation) {
            reservations.add(reservation);
        }

        public List<Reservation> getAllReservations() {
            return Collections.unmodifiableList(reservations); // read-only
        }
    }

    // Report Service
    static class BookingReportService {

        public int getTotalBookings(List<Reservation> reservations) {
            return reservations.size();
        }

        public double getTotalRevenue(List<Reservation> reservations) {
            double total = 0;
            for (Reservation r : reservations) {
                total += r.getPrice();
            }
            return total;
        }

        public void generateReport(List<Reservation> reservations) {
            System.out.println("---- Booking Report ----");
            System.out.println("Total Bookings: " + getTotalBookings(reservations));
            System.out.println("Total Revenue: ₹" + getTotalRevenue(reservations));

            System.out.println("\nBooking Details:");
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

    // Main Method (Flow)
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        history.addReservation(new Reservation("R001", "Ram", "Deluxe", 3000));
        history.addReservation(new Reservation("R002", "Lokesh", "Suite", 5000));
        history.addReservation(new Reservation("R003", "Arun", "Standard", 2000));

        // Admin requests report
        reportService.generateReport(history.getAllReservations());
    }
}
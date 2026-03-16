import java.util.*;

/**
 * BookMyStayApp
 *
 * Demonstrates add-on service selection for reservations.
 * Guests can attach optional services to an existing reservation
 * without modifying the core booking or inventory state.
 *
 * @author Ram Lokesh
 * @version 1.0
 */
public class BookMyStayApp {

    /**
     * AddOnService represents an optional service offered to guests
     */
    static class AddOnService {

        String serviceName;
        int price;

        public AddOnService(String serviceName, int price) {
            this.serviceName = serviceName;
            this.price = price;
        }
    }

    public static void main(String[] args) {

        // Map: Reservation ID -> List of selected services
        Map<String, List<AddOnService>> reservationServices = new HashMap<>();

        // Example reservation ID
        String reservationId = "RES-101";

        // Guest selects add-on services
        List<AddOnService> selectedServices = new ArrayList<>();

        selectedServices.add(new AddOnService("Breakfast", 500));
        selectedServices.add(new AddOnService("Airport Pickup", 1200));
        selectedServices.add(new AddOnService("Spa Access", 1500));

        // Map services to reservation
        reservationServices.put(reservationId, selectedServices);

        System.out.println("----- ADD-ON SERVICES SELECTED -----");

        int totalCost = 0;

        // Retrieve services for reservation
        List<AddOnService> services = reservationServices.get(reservationId);

        for (AddOnService service : services) {

            System.out.println("Service: " + service.serviceName +
                    " | Cost: ₹" + service.price);

            totalCost += service.price;
        }

        System.out.println("\nTotal Add-On Cost for Reservation "
                + reservationId + ": ₹" + totalCost);

        System.out.println("\nCore booking and inventory state remain unchanged.");
    }
}
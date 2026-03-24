import java.util.*;


public class BookMyStayApp {

    
    static class AddOnService {

        String serviceName;
        int price;

        public AddOnService(String serviceName, int price) {
            this.serviceName = serviceName;
            this.price = price;
        }
    }

    public static void main(String[] args) {

        Map<String, List<AddOnService>> reservationServices = new HashMap<>();

        String reservationId = "RES-101";

        List<AddOnService> selectedServices = new ArrayList<>();

        selectedServices.add(new AddOnService("Breakfast", 500));
        selectedServices.add(new AddOnService("Airport Pickup", 1200));
        selectedServices.add(new AddOnService("Spa Access", 1500));

        reservationServices.put(reservationId, selectedServices);

        System.out.println("----- ADD-ON SERVICES SELECTED -----");

        int totalCost = 0;

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

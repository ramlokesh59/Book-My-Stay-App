import java.util.*;

// Main Class
public class BookMyStayApp {

    // Booking Request
    static class BookingRequest {
        String guestName;
        String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // Shared Booking Queue
    static class BookingQueue {
        private Queue<BookingRequest> queue = new LinkedList<>();

        public synchronized void addRequest(BookingRequest request) {
            queue.add(request);
        }

        public synchronized BookingRequest getRequest() {
            return queue.poll();
        }
    }

    // Booking Service (Thread-Safe)
    static class BookingService {
        private Map<String, Integer> inventory = new HashMap<>();

        public BookingService() {
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
        }

        // Critical Section
        public synchronized void processBooking(BookingRequest request) {
            String type = request.roomType;

            if (!inventory.containsKey(type) || inventory.get(type) <= 0) {
                System.out.println(Thread.currentThread().getName() +
                        " → Booking FAILED for " + request.guestName +
                        " (" + type + ")");
                return;
            }

            // Simulate delay (to expose race conditions if not synchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(type, inventory.get(type) - 1);

            System.out.println(Thread.currentThread().getName() +
                    " → Booking SUCCESS for " + request.guestName +
                    " (" + type + ")");
        }

        public void showInventory() {
            System.out.println("\nFinal Inventory: " + inventory);
        }
    }

    // Worker Thread
    static class BookingProcessor extends Thread {
        private BookingQueue queue;
        private BookingService service;

        public BookingProcessor(BookingQueue queue, BookingService service, String name) {
            super(name);
            this.queue = queue;
            this.service = service;
        }

        public void run() {
            while (true) {
                BookingRequest request;

                synchronized (queue) {
                    request = queue.getRequest();
                }

                if (request == null) break;

                service.processBooking(request);
            }
        }
    }

    // Main Logic
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        BookingService service = new BookingService();

        // Simulate multiple requests
        queue.addRequest(new BookingRequest("Ram", "Deluxe"));
        queue.addRequest(new BookingRequest("Lokesh", "Deluxe"));
        queue.addRequest(new BookingRequest("Arun", "Deluxe")); // extra → should fail

        queue.addRequest(new BookingRequest("Kumar", "Suite"));
        queue.addRequest(new BookingRequest("Vijay", "Suite")); // extra → should fail

        // Multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor(queue, service, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, service, "Thread-2");
        BookingProcessor t3 = new BookingProcessor(queue, service, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        // Final state
        service.showInventory();
    }
}
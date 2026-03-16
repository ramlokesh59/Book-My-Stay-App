
public abstract class BookMyStayApp {

    protected String roomType;
    protected int beds;
    protected double size;
    protected double price;

    /**
     * Constructor to initialize common room attributes
     */
    public BookMyStayApp(String roomType, int beds, double size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price + " per night");
    }
}
import java.util.*;

/* =====================================================
CLASS - Reservation
===================================================== */

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

}

/* =====================================================
CLASS - RoomInventory (SHARED RESOURCE)
===================================================== */

class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 1);
        availability.put("Double", 1);
    }

    // synchronized → critical section
    public synchronized boolean bookRoom(String roomType) {

        int available = availability.getOrDefault(roomType, 0);

        if (available > 0) {
            availability.put(roomType, available - 1);
            return true;
        }

        return false;
    }

}

/* =====================================================
CLASS - BookingTask (THREAD)
===================================================== */

class BookingTask implements Runnable {

    private Reservation reservation;
    private RoomInventory inventory;

    public BookingTask(Reservation reservation, RoomInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        String guest = reservation.getGuestName();
        String type = reservation.getRoomType();

        if (inventory.bookRoom(type)) {
            System.out.println("Booking SUCCESS for " + guest + " (" + type + ")");
        } else {
            System.out.println("Booking FAILED for " + guest + " (" + type + ")");
        }
    }

}

/* =====================================================
MAIN CLASS - BookMyStayApp
===================================================== */

public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        RoomInventory inventory = new RoomInventory();

        // Simulating multiple users booking SAME room type
        Thread t1 = new Thread(new BookingTask(new Reservation("Abhi", "Single"), inventory));
        Thread t2 = new Thread(new BookingTask(new Reservation("Subha", "Single"), inventory));
        Thread t3 = new Thread(new BookingTask(new Reservation("Kiran", "Double"), inventory));
        Thread t4 = new Thread(new BookingTask(new Reservation("Ravi", "Double"), inventory));

        // Start threads (concurrent execution)
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

}

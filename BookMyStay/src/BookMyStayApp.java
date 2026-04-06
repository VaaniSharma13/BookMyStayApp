import java.io.*;
import java.util.*;


class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
CLASS - SystemState (Serializable)
===================================================== */

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

/* =====================================================
CLASS - PersistenceService
===================================================== */

class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    // SAVE
    public static void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // LOAD
    public static SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) in.readObject();
            System.out.println("System state loaded successfully.");
            return state;
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }
    }

}

/* =====================================================
MAIN CLASS - BookMyStayApp
===================================================== */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Data Persistence & Recovery\n");

        // Try loading previous state
        SystemState state = PersistenceService.load();

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        if (state == null) {
            // Fresh start
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 1);

            bookings = new ArrayList<>();

            System.out.println("Initialized new system state.");
        } else {
            // Restore state
            inventory = state.inventory;
            bookings = state.bookings;

            System.out.println("Recovered Inventory: " + inventory);
            System.out.println("Recovered Bookings: " + bookings.size());
        }

        // Simulate booking
        Reservation r = new Reservation("Abhi", "Single");

        if (inventory.get("Single") > 0) {
            bookings.add(r);
            inventory.put("Single", inventory.get("Single") - 1);
            System.out.println("Booking added for " + r.getGuestName());
        } else {
            System.out.println("No rooms available.");
        }

        // Save state before shutdown
        PersistenceService.save(new SystemState(inventory, bookings));
    }
}

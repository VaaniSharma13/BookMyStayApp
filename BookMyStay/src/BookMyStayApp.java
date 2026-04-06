import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

}

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 1);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void increase(String type) {
        availability.put(type, getAvailability(type) + 1);
    }

    public void decrease(String type) {
        availability.put(type, getAvailability(type) - 1);
    }

}

/* =====================================================
CLASS - BookingHistory
===================================================== */

class BookingHistory {
    private List<Reservation> bookings = new ArrayList<>();

    public void add(Reservation r) {
        bookings.add(r);
    }

    public boolean remove(Reservation r) {
        return bookings.remove(r);
    }

    public boolean exists(String roomId) {
        for (Reservation r : bookings) {
            if (r.getRoomId().equals(roomId)) return true;
        }
        return false;
    }

    public Reservation getByRoomId(String roomId) {
        for (Reservation r : bookings) {
            if (r.getRoomId().equals(roomId)) return r;
        }
        return null;
    }

}

/* =====================================================
CLASS - CancellationService
===================================================== */

class CancellationService {
    private Stack<String> rollbackStack = new Stack<>();

    public void cancel(String roomId, BookingHistory history, RoomInventory inventory) {

        // Validate existence
        if (!history.exists(roomId)) {
            System.out.println("Cancellation failed: Reservation not found for " + roomId);
            return;
        }

        Reservation r = history.getByRoomId(roomId);

        // Push to rollback stack (LIFO)
        rollbackStack.push(roomId);

        // Remove from history
        history.remove(r);

        // Restore inventory
        inventory.increase(r.getRoomType());

        System.out.println("Cancelled booking for " + r.getGuestName() +
                ", Room ID: " + roomId);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

/* =====================================================
MAIN CLASS - BookMyStayApp
===================================================== */

public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("Booking Cancellation & Rollback\n");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("Abhi", "Single", "Single-1");
        Reservation r2 = new Reservation("Subha", "Double", "Double-1");

        history.add(r1);
        history.add(r2);

        // Cancel valid booking
        cancelService.cancel("Single-1", history, inventory);

        // Attempt invalid cancellation
        cancelService.cancel("Suite-99", history, inventory);

        // Show rollback history
        cancelService.showRollbackStack();
    }

}

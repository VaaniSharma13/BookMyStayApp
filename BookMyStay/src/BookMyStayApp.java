import java.util.*;

/* =====================================================
CUSTOM EXCEPTION - InvalidBookingException
===================================================== */

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

}

/* =====================================================
CLASS - RoomInventory
===================================================== */

class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 1);
        roomAvailability.put("Double", 1);
        roomAvailability.put("Suite", 0); // purposely 0 to trigger error
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, -1);
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

}

/* =====================================================
CLASS - BookingValidator
===================================================== */

class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        String roomType = reservation.getRoomType();

        // Validate room type
        if (!Arrays.asList("Single", "Double", "Suite").contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate availability
        int available = inventory.getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }

}

/* =====================================================
CLASS - BookingService
===================================================== */

class BookingService {

    public void processBooking(Reservation reservation, RoomInventory inventory) {

        try {
            // Validate first (fail-fast)
            BookingValidator.validate(reservation, inventory);

            // If valid → proceed
            int remaining = inventory.getAvailability(reservation.getRoomType()) - 1;
            inventory.updateAvailability(reservation.getRoomType(), remaining);

            System.out.println(
                    "Booking successful for " + reservation.getGuestName() +
                            " (" + reservation.getRoomType() + ")"
            );

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

/* =====================================================
MAIN CLASS - BookMyStayApp
===================================================== */

public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("Booking Validation & Error Handling\n");

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        // Valid booking
        service.processBooking(new Reservation("Abhi", "Single"), inventory);

        // Invalid room type
        service.processBooking(new Reservation("Subha", "Deluxe"), inventory);

        // No availability case
        service.processBooking(new Reservation("Vanmathi", "Suite"), inventory);

        // Negative / exhausted case
        service.processBooking(new Reservation("Kiran", "Single"), inventory);
    }

}

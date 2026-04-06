import java.util.*;

/* =====================================================
CLASS - Reservation
===================================================== */

class Reservation {

    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

}

/* =====================================================
CLASS - BookingHistory
===================================================== */

class BookingHistory {

    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}

/* =====================================================
CLASS - BookingReportService
===================================================== */

class BookingReportService {
    public void displayAllBookings(BookingHistory history) {

        System.out.println("\nBooking History:");

        for (Reservation r : history.getAllBookings()) {
            System.out.println(
                    "Guest: " + r.getGuestName() +
                            ", Room Type: " + r.getRoomType() +
                            ", Room ID: " + r.getRoomId()
            );
        }
    }

    public void generateSummaryReport(BookingHistory history) {

        Map<String, Integer> countByType = new HashMap<>();

        for (Reservation r : history.getAllBookings()) {
            countByType.put(
                    r.getRoomType(),
                    countByType.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\nBooking Summary Report:");

        for (String type : countByType.keySet()) {
            System.out.println(type + " Rooms Booked: " + countByType.get(type));
        }
    }

}

public class BookMyStayApp{

    public static void main(String[] args) {

        System.out.println("Booking History & Reporting");

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings (from Use Case 6)
        history.addBooking(new Reservation("Abhi", "Single", "Single-1"));
        history.addBooking(new Reservation("Subha", "Double", "Double-1"));
        history.addBooking(new Reservation("Vanmathi", "Suite", "Suite-1"));

        // Display all bookings
        reportService.displayAllBookings(history);

        // Generate summary report
        reportService.generateSummaryReport(history);
    }

}

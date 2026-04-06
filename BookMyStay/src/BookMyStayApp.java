import java.util.*;


class Service {

    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

}
class AddOnServiceManager {

    private Map<String, List<Service>> servicesByReservation;

    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    public void addService(String reservationId, Service service) {
        servicesByReservation.putIfAbsent(reservationId, new ArrayList<>());
        servicesByReservation.get(reservationId).add(service);
    }

    public double calculateTotalServiceCost(String reservationId) {

        List<Service> services = servicesByReservation.getOrDefault(reservationId, new ArrayList<>());

        double total = 0;

        for (Service s : services) {
            total += s.getCost();
        }

        return total;
    }

}
public class BookMyStayApp {
    public static void main(String[] args) {

        System.out.println("Add-On Service Selection");

        // Assume reservation already confirmed (from Use Case 6)
        String reservationId = "Single-1";

        System.out.println("Reservation ID: " + reservationId);

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservationId, new Service("Breakfast", 500));
        manager.addService(reservationId, new Service("Spa", 1000));

        double totalCost = manager.calculateTotalServiceCost(reservationId);

        System.out.println("Total Add-On Cost: " + totalCost);
    }

}

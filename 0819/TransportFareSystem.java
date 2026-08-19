abstract class Transport {
    private final String routeName;

    Transport(String routeName) {
        this.routeName = (routeName == null || routeName.trim().isEmpty()) ? "Unknown Route" : routeName.trim();
    }

    public String getRouteName() {
        return routeName;
    }

    abstract int calculateFare(int distance);
}

class Bus extends Transport {
    Bus(String routeName) {
        super(routeName);
    }

    @Override
    int calculateFare(int distance) {
        int safeDistance = Math.max(0, distance);
        // Base fare 15, plus 5 for every 5 km
        return 15 + (safeDistance / 5) * 5;
    }
}

class Taxi extends Transport {
    Taxi(String routeName) {
        super(routeName);
    }

    @Override
    int calculateFare(int distance) {
        int safeDistance = Math.max(0, distance);
        if (safeDistance == 0) {
            return 0;
        }
        // Base fare 70 for first km, and 15 for each subsequent km
        return 70 + (safeDistance - 1) * 15;
    }
}

public class TransportFareSystem {
    public static void main(String[] args) {
        Transport[] transports = {
            new Bus("Red 23"),
            new Bus("Blue 15"),
            new Taxi("Taxi-001"),
            new Taxi("Taxi-002")
        };

        int[] testDistances = {0, 3, 10, 25};

        System.out.println("=== Transport Fare System ===");
        for (Transport transport : transports) {
            System.out.println("\nRoute: " + transport.getRouteName());
            for (int dist : testDistances) {
                System.out.printf("  Distance: %d km -> Fare: %d 元%n", dist, transport.calculateFare(dist));
            }
        }
    }
}

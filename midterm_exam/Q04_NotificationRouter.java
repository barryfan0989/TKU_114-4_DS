import java.util.ArrayList;
import java.util.List;

public class Q04_NotificationRouter {
    public interface Channel {
        String name();

        boolean supports(String destination);

        String send(String destination, String message);
    }

    public static class EmailChannel implements Channel {
        @Override
        public String name() {
            return "EMAIL";
        }

        @Override
        public boolean supports(String destination) {
            if (destination == null) {
                return false;
            }
            int atIndex = destination.indexOf('@');
            return atIndex > 0 && atIndex < destination.length() - 1;
        }

        @Override
        public String send(String destination, String message) {
            return name() + "|" + destination + "|" + message;
        }
    }

    public static class SmsChannel implements Channel {
        @Override
        public String name() {
            return "SMS";
        }

        @Override
        public boolean supports(String destination) {
            if (destination == null) {
                return false;
            }
            String cleaned = destination.replace("-", "");
            if (cleaned.length() != 10) {
                return false;
            }
            for (char c : cleaned.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String send(String destination, String message) {
            return name() + "|" + destination + "|" + message;
        }
    }

    public static List<String> route(List<Channel> channels, String destination, String message) {
        List<String> results = new ArrayList<>();
        if (channels == null || destination == null || message == null) {
            return results;
        }
        for (Channel channel : channels) {
            if (channel != null && channel.supports(destination)) {
                results.add(channel.send(destination, message));
            }
        }
        return results;
    }
}

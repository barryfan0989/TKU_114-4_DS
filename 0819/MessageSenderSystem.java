interface MessageSender {
    void send(String receiver, String message);
}

class EmailSender implements MessageSender {
    @Override
    public void send(String receiver, String message) {
        System.out.println("EMAIL to " + receiver + " -> " + message);
    }
}

class SmsSender implements MessageSender {
    @Override
    public void send(String receiver, String message) {
        System.out.println("SMS to " + receiver + " -> " + message);
    }
}

class ConsoleSender implements MessageSender {
    @Override
    public void send(String receiver, String message) {
        System.out.println("CONSOLE " + receiver + " -> " + message);
    }
}

public class MessageSenderSystem {
    public static void notify(MessageSender sender, String receiver, String message) {
        if (sender == null) {
            System.out.println("[Error] Sender cannot be null.");
            return;
        }
        if (receiver == null || receiver.trim().isEmpty()) {
            System.out.println("[Warning] Receiver is empty. Transmission aborted.");
            return;
        }
        if (message == null || message.trim().isEmpty()) {
            System.out.println("[Warning] Message is empty. Transmission aborted.");
            return;
        }
        sender.send(receiver.trim(), message.trim());
    }

    public static void main(String[] args) {
        MessageSender email = new EmailSender();
        MessageSender sms = new SmsSender();
        MessageSender console = new ConsoleSender();

        System.out.println("=== Message Sender System ===");
        
        // Normal cases
        System.out.println("\n--- Normal transmission ---");
        notify(email, "amy@example.com", "Your code is approved.");
        notify(sms, "0912345678", "Code runs fine.");
        notify(console, "Admin", "System startup.");

        // Border cases
        System.out.println("\n--- Border cases (empty/null checks) ---");
        notify(email, null, "Hello");
        notify(sms, "0912345678", "");
        notify(console, "  ", "Whitespace test");
        notify(email, "ben@example.com", "   ");
    }
}

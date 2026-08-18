import java.util.Objects;

class LibraryMember {
    private String memberId;
    private String name;
    private String email;

    LibraryMember(String memberId, String name, String email) {
        this.memberId = memberId == null || memberId.trim().isEmpty() ? "Unknown" : memberId.trim();
        this.name = name == null || name.trim().isEmpty() ? "Unknown" : name.trim();
        this.email = email == null || email.trim().isEmpty() ? "Unknown" : email.trim();
    }

    @Override
    public String toString() {
        return "LibraryMember{memberId='" + memberId + "', name='" + name + "', email='" + email + "'}";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        LibraryMember member = (LibraryMember) other;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}

public class MemberEqualityPractice {
    public static void main(String[] args) {
        LibraryMember m1 = new LibraryMember("M001", "Amy", "amy@example.com");
        LibraryMember m2 = new LibraryMember("M001", "Amy Chen", "amy.chen@example.com");
        LibraryMember m3 = null;

        System.out.println("m1: " + m1);
        System.out.println("m2: " + m2);

        System.out.println("\nComparison results:");
        System.out.println("m1 == m2: " + (m1 == m2));
        System.out.println("m1.equals(m2): " + m1.equals(m2));
        
        System.out.println("\nNull comparison safety check:");
        try {
            System.out.println("m1.equals(m3): " + m1.equals(m3));
            System.out.println("m1.equals(null): " + m1.equals(null));
        } catch (NullPointerException e) {
            System.out.println("Fails: NullPointerException thrown!");
        }
    }
}

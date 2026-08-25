class Member {
    int memberId;
    String name;
    String email;

    Member(int memberId, String name, String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or blank.");
        }
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member ID: " + memberId + " | Name: " + name + " | Email: " + email;
    }
}

class MemberNode {
    Member member;
    MemberNode left;
    MemberNode right;

    MemberNode(Member member) {
        this.member = member;
    }
}

class MemberBst {
    MemberNode root;

    boolean add(Member member) {
        if (member == null) return false;
        if (root == null) {
            root = new MemberNode(member);
            return true;
        }
        MemberNode current = root;
        while (true) {
            if (member.memberId == current.member.memberId) {
                System.out.println("  [Error] Duplicate Member ID: " + member.memberId);
                return false;
            }
            if (member.memberId < current.member.memberId) {
                if (current.left == null) {
                    current.left = new MemberNode(member);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new MemberNode(member);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Member find(int memberId) {
        MemberNode current = root;
        while (current != null) {
            if (memberId == current.member.memberId) {
                return current.member;
            }
            current = (memberId < current.member.memberId) ? current.left : current.right;
        }
        return null;
    }

    boolean updateEmail(int memberId, String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            System.out.println("  [Error] Cannot update to a blank email for Member ID: " + memberId);
            return false;
        }
        Member m = find(memberId);
        if (m == null) {
            System.out.println("  [Error] Member ID " + memberId + " not found.");
            return false;
        }
        m.email = newEmail;
        System.out.println("  [Success] Updated email for Member ID " + memberId + " to: " + newEmail);
        return true;
    }

    boolean remove(int memberId) {
        if (find(memberId) == null) {
            System.out.println("  [Error] Cannot remove: Member ID " + memberId + " not found.");
            return false;
        }
        root = remove(root, memberId);
        return true;
    }

    private MemberNode remove(MemberNode node, int memberId) {
        if (node == null) return null;
        if (memberId < node.member.memberId) {
            node.left = remove(node.left, memberId);
        } else if (memberId > node.member.memberId) {
            node.right = remove(node.right, memberId);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            MemberNode successor = minimumNode(node.right);
            node.member = successor.member;
            node.right = remove(node.right, successor.member.memberId);
        }
        return node;
    }

    private MemberNode minimumNode(MemberNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    void inorderReport() {
        inorder(root);
    }

    private void inorder(MemberNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.member);
        inorder(node.right);
    }
}

public class MemberBstIndex {
    public static void main(String[] args) {
        MemberBst index = new MemberBst();

        System.out.println("=== 1. Adding Members ===");
        try {
            index.add(new Member(102, "Alice", "alice@example.com"));
            index.add(new Member(101, "Bob", "bob@example.com"));
            index.add(new Member(103, "Charlie", "charlie@example.com"));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Test Duplicate Rejection
        try {
            index.add(new Member(102, "Duplicate Alice", "dup_alice@example.com"));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Test Blank Email Rejection on add
        try {
            System.out.print("Creating Member with empty email: ");
            new Member(104, "David", "   ");
        } catch (Exception e) {
            System.out.println("Exception caught -> " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== 2. Current Member Directory ===");
        index.inorderReport();
        System.out.println();

        System.out.println("=== 3. Search Member ===");
        Member found = index.find(101);
        System.out.println("Find ID 101: " + (found != null ? found : "NOT FOUND"));
        System.out.println("Find ID 999: " + (index.find(999) != null ? index.find(999) : "NOT FOUND"));
        System.out.println();

        System.out.println("=== 4. Updating Member Email ===");
        index.updateEmail(101, "new_bob@example.com");
        index.updateEmail(101, ""); // should fail
        System.out.println();

        System.out.println("=== 5. Deleting Member ===");
        index.remove(102);
        index.remove(999); // should fail
        System.out.println();

        System.out.println("=== 6. Final Member Directory ===");
        index.inorderReport();
    }
}

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Q01_PriorityRecord {
    public record Job(String id, int priority, long sequence) {}

    public static List<String> processOrder(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        Comparator<Job> comparator = (a, b) -> {
            int p = Integer.compare(a.priority(), b.priority());
            if (p != 0) {
                return p;
            }
            int s = Long.compare(a.sequence(), b.sequence());
            if (s != 0) {
                return s;
            }
            if (a.id() == null && b.id() == null) {
                return 0;
            }
            if (a.id() == null) {
                return -1;
            }
            if (b.id() == null) {
                return 1;
            }
            return a.id().compareTo(b.id());
        };

        PriorityQueue<Job> pq = new PriorityQueue<>(comparator);
        for (Job job : jobs) {
            if (job != null) {
                pq.offer(job);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            Job job = pq.poll();
            result.add(job.id());
        }

        return result;
    }
}

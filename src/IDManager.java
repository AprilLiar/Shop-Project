import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IDManager {
    private static final Set<Long> usedIds = new HashSet<>();
    private static final Random random = new Random();

    public static long generateUniqueId() {
        long id;
        do {
            id = (long) (random.nextDouble() * Math.pow(10, 10));
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }
}

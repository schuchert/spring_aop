package shoe.example.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class CorrelationId {
    private static final AtomicInteger uniqueId = new AtomicInteger(1);
    private static ThreadLocal<Integer> id = new ThreadLocal<Integer>();
    private static ThreadLocal<Integer> level = new ThreadLocal<Integer>();

    public static void enter() {
        if (id.get() == null) {
            id.set(uniqueId.getAndIncrement());
            level.set(0);
        }
        level.set(level.get() + 1);
    }

    public static String get() {
        if (id.get() == null) {
            throw new RuntimeException("get called outside of enter/exit sequence");
        }

        return String.format("%d.%d", id.get(), level.get());
    }

    public static void exit() {
        if (id.get() != null) {
            level.set(level.get() - 1);
            if (level.get() < 1) {
                id.remove();
                level.remove();
            }
        } else {
            throw new RuntimeException("exit called before enter");
        }
    }
}

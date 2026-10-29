package cinema;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Консольний журнал подій у форматі [час] [джерело] подія. Час умовний. */
public final class Logger {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static LocalTime clock = LocalTime.of(18, 0, 0);

    private Logger() {}

    public static synchronized void log(String source, String event) {
        clock = clock.plusSeconds(1);
        System.out.printf("[%s] [%s] %s%n", clock.format(FMT), source, event);
    }

    public static void expensive(String source, String event, int millis) {
        log(source, event + " — розпочато...");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        synchronized (Logger.class) {
            clock = clock.plusSeconds(millis / 100);
        }
        log(source, event + " — готово");
    }
}

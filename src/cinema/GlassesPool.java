package cinema;

import cinema.domain.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Пул 3D-окулярів. Роль у Object Pool: ObjectPool.
 *
 * Усі пари створюються один раз (закупівля — «дорога» операція).
 * Нові пари після цього не з'являються: їх кількість обмежена capacity.
 * Стратегія при браку окулярів: глядач стає у FIFO-чергу очікування
 * і отримує пару, щойно вона повертається.
 */
public class GlassesPool {
    private static final String SRC = "Пул окулярів";

    private final List<Glasses> all = new ArrayList<>();
    private final Deque<Glasses> free = new ArrayDeque<>();
    private final Deque<String> waiting = new ArrayDeque<>();

    public GlassesPool(int capacity) {
        for (int i = 1; i <= capacity; i++) {
            Logger.expensive(SRC, "Закупівля пари G-" + i, 200);
            Glasses g = new Glasses("G-" + i);
            all.add(g);
            free.addLast(g);
        }
        Logger.log(SRC, "Пул створено, загальна кількість пар: " + capacity);
    }

    /**
     * Видати окуляри глядачеві на місці seat.
     * @return окуляри або null, якщо вільних немає (глядач поставлений у чергу)
     */
    public synchronized Glasses acquire(String seat) {
        Glasses g = free.pollFirst();
        if (g == null) {
            waiting.addLast(seat);
            Logger.log(SRC, "Вільних окулярів немає: місце " + seat
                    + " очікує повернення (у черзі: " + waiting.size() + ")");
            return null;
        }
        g.issueTo(seat);
        Logger.log(SRC, "Окуляри " + g.getId() + " → місце " + seat);
        return g;
    }

    /**
     * Повернути окуляри: дезінфекція, скидання прив'язки, повернення у пул.
     * Якщо хтось очікує — окуляри одразу видаються першому з черги.
     * @return місце глядача з черги, який отримав ці окуляри, або null
     */
    public synchronized String release(Glasses g) {
        if (!all.contains(g) || !g.isIssued()) {
            Logger.log(SRC, "Відмова: окуляри " + g.getId() + " не були видані");
            return null;
        }
        Logger.expensive(SRC, "Дезінфекція " + g.getId(), 100);
        g.disinfectAndReset();
        Logger.log(SRC, g.getId() + " повернено, продезінфіковано");

        String next = waiting.pollFirst();
        if (next == null) {
            free.addLast(g);
            return null;
        }
        g.issueTo(next);
        Logger.log(SRC, "Окуляри " + g.getId() + " → місце " + next + " (з черги очікування)");
        return next;
    }

    public synchronized int getCapacity() { return all.size(); }
    public synchronized int getFreeCount() { return free.size(); }
    public synchronized int getIssuedCount() { return all.size() - free.size(); }
    public synchronized int getWaitingCount() { return waiting.size(); }

    /** Статистика: кількість видач кожної пари і загальна кількість пар. */
    public synchronized void printStatistics() {
        Logger.log(SRC, "=== Статистика ===");
        for (Glasses g : all) {
            Logger.log(SRC, "Пара " + g.getId() + ": видач — " + g.getIssueCount());
        }
        Logger.log(SRC, "Загальна кількість пар: " + all.size());
    }
}
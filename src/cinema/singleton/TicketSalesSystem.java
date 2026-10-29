package cinema;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TicketSalesSystem {
    private final String id;
    private final Map<String, Session> sessions = new HashMap<>();

    private TicketSalesSystem() {
        this.id = "TSS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Logger.expensive("Система продажу", "Підключення до обліку місць", 300);
        Logger.log("Система продажу", "Систему продажу ініціалізовано");
    }

    private static final class Holder {
        private static final TicketSalesSystem INSTANCE = new TicketSalesSystem();
    }

    public static TicketSalesSystem getInstance() {
        return Holder.INSTANCE;
    }

    public String getId() { return id; }

    public synchronized void registerSession(Session session) {
        sessions.put(session.getId(), session);
        Logger.log("Система продажу", "Сеанс зареєстровано: " + session.getId());
    }

    public synchronized boolean sellSeat(Channel channel, String sessionId, int row, int number) {
        String src = channel.getTitle();
        Session session = sessions.get(sessionId);
        Seat seat = (session == null) ? null : session.getSeat(row, number);
        if (seat == null) {
            Logger.log(src, "Відмова: сеанс або місце " + row + "-" + number + " не існує");
            return false;
        }
        if (seat.isSold()) {
            Logger.log(src, "Відмова: Місце " + seat.label() + " уже продано (" + seat.getSoldVia().getTitle() + ")");
            return false;
        }
        session.sell(channel, row, number);
        Logger.log(src, "Продано місце " + seat.label() + " на сеанс " + sessionId);
        return true;
    }
}

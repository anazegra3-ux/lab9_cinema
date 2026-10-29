package cinema;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/** Реєстр сеансів-зразків (за ключем «фільм|зала»). Видає незалежні копії. */
public class SessionPrototypeRegistry {
    private final Map<String, Session> prototypes = new HashMap<>();

    public static String key(String film, int hallNumber) {
        return film + "|" + hallNumber;
    }

    public void register(String key, Session prototype) {
        prototypes.put(key, prototype);
        Logger.log("Реєстр зразків", "Зареєстровано сеанс-зразок «" + key + "»");
    }

    public Session create(String key, LocalDateTime start) {
        Session proto = prototypes.get(key);
        if (proto == null) throw new IllegalArgumentException("Немає зразка: " + key);
        Session s = proto.copy();
        s.setStart(start);
        Logger.log("Реєстр зразків", "Сеанс " + s.getId() + " створено клонуванням, без повторного налаштування");
        return s;
    }
}

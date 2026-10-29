package cinema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Сеанс. Роль у Prototype: ConcretePrototype.
 * Містить вкладені об'єкти (місця, ціни, реклама), тому копіювання глибоке.
 */
public class Session implements Prototype<Session> {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM HH:mm");

    private final String film;
    private final Hall hall;                          // незмінний, ділиться
    private LocalDateTime start;
    private final Map<String, Double> prices;         // категорія -> ціна
    private final List<AdClip> adBlock;
    private final Seat[][] seats;
    private boolean rightsVerified;

    public Session(String film, Hall hall, LocalDateTime start) {
        String src = "Сеанс-зразок";
        this.film = film;
        this.hall = hall;
        this.start = start;

        Logger.expensive(src, "Завантаження схеми зали " + hall.getNumber(), 300);
        this.seats = new Seat[hall.getRows()][hall.getSeatsPerRow()];
        for (int r = 1; r <= hall.getRows(); r++) {
            for (int n = 1; n <= hall.getSeatsPerRow(); n++) {
                seats[r - 1][n - 1] = new Seat(r, n, hall.categoryForRow(r));
            }
        }

        Logger.expensive(src, "Налаштування цін за категоріями місць", 300);
        this.prices = new LinkedHashMap<>();
        prices.put("Перший ряд", 130.0);
        prices.put("Стандарт", 150.0);
        prices.put("Люкс", 250.0);

        Logger.expensive(src, "Формування рекламного блоку", 300);
        this.adBlock = new ArrayList<>();
        adBlock.add(new AdClip("Кава «Ранок»", 30));
        adBlock.add(new AdClip("Авто «Місто»", 20));

        Logger.expensive(src, "Перевірка прав на показ", 300);
        this.rightsVerified = true;
    }

    private Session(Session o) {
        this.film = o.film;
        this.hall = o.hall;
        this.start = o.start;
        this.prices = new LinkedHashMap<>(o.prices);
        this.adBlock = new ArrayList<>();
        for (AdClip c : o.adBlock) adBlock.add(new AdClip(c));
        this.seats = new Seat[o.seats.length][];
        for (int r = 0; r < o.seats.length; r++) {
            this.seats[r] = new Seat[o.seats[r].length];
            for (int n = 0; n < o.seats[r].length; n++) {
                this.seats[r][n] = new Seat(o.seats[r][n]);
            }
        }
        this.rightsVerified = o.rightsVerified;
    }

    @Override
    public Session copy() {
        return new Session(this);
    }

    public String getId() {
        return film + " / зала " + hall.getNumber() + " / " + start.format(FMT);
    }

    public void setStart(LocalDateTime start) { this.start = start; }
    public String getFilm() { return film; }

    public Seat getSeat(int row, int number) {
        if (row < 1 || row > seats.length || number < 1 || number > seats[0].length) return null;
        return seats[row - 1][number - 1];
    }

    public boolean sell(Channel channel, int row, int number) {
        Seat s = getSeat(row, number);
        if (s == null || s.isSold()) return false;
        s.sell(channel);
        return true;
    }

    public void setPrice(String category, double price) { prices.put(category, price); }
    public void replaceAd(int index, AdClip clip) { adBlock.set(index, clip); }

    /** Текстовий опис стану сеансу (для виводу «поруч»). */
    public List<String> describe() {
        List<String> out = new ArrayList<>();
        out.add("Сеанс: " + getId());
        out.add("Ціни: " + prices);
        out.add("Реклама: " + adBlock);
        List<String> sold = new ArrayList<>();
        for (Seat[] row : seats)
            for (Seat s : row)
                if (s.isSold()) sold.add(s.label() + "(" + s.getSoldVia().getTitle() + ")");
        out.add("Продані місця: " + (sold.isEmpty() ? "—" : String.join(", ", sold)));
        return out;
    }
}

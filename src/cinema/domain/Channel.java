package cinema;

/** Канал продажу квитків. */
public enum Channel {
    CASHIER("каса"), WEBSITE("сайт"), APP("застосунок");

    private final String title;

    Channel(String title) { this.title = title; }

    public String getTitle() { return title; }
}

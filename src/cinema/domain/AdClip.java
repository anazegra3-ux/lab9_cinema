package cinema.domain;

/** Рекламний ролик у рекламному блоці сеансу. */
public class AdClip {
    private final String title;
    private final int seconds;

    public AdClip(String title, int seconds) {
        this.title = title;
        this.seconds = seconds;
    }

    public AdClip(AdClip other) {
        this(other.title, other.seconds);
    }

    @Override
    public String toString() { return title + " (" + seconds + "с)"; }
}

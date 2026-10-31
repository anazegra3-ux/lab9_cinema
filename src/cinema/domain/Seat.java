package cinema.domain;

/** Місце в залі на конкретному сеансі (стан належить сеансу). */
public class Seat {
    private final int row;
    private final int number;
    private final String category;
    private boolean sold;
    private Channel soldVia;

    public Seat(int row, int number, String category) {
        this.row = row;
        this.number = number;
        this.category = category;
    }

    public Seat(Seat other) {
        this.row = other.row;
        this.number = other.number;
        this.category = other.category;
        this.sold = other.sold;
        this.soldVia = other.soldVia;
    }

    public void sell(Channel channel) {
        this.sold = true;
        this.soldVia = channel;
    }

    public boolean isSold() { return sold; }
    public Channel getSoldVia() { return soldVia; }
    public String getCategory() { return category; }
    public String label() { return row + "-" + number; }
}

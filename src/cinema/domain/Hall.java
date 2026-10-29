package cinema;

/** Зала: незмінна схема місць (тому її безпечно ділити між сеансами). */
public final class Hall {
    private final int number;
    private final int rows;
    private final int seatsPerRow;

    public Hall(int number, int rows, int seatsPerRow) {
        this.number = number;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public int getNumber() { return number; }
    public int getRows() { return rows; }
    public int getSeatsPerRow() { return seatsPerRow; }

    public String categoryForRow(int row) {
        if (row == 1) return "Перший ряд";
        if (row == rows) return "Люкс";
        return "Стандарт";
    }
}

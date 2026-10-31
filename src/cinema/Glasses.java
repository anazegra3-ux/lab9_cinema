package cinema;

/**
 * 3D-окуляри. Роль у Object Pool: Reusable (повторно використовуваний об'єкт).
 * Конструктор пакетний: створювати окуляри може лише пул (GlassesPool).
 */
public class Glasses {
    private final String id;
    private int issueCount;
    private String assignedSeat;   // null, якщо окуляри вільні

    Glasses(String id) {
        this.id = id;
    }

    void issueTo(String seat) {
        this.assignedSeat = seat;
        this.issueCount++;
    }

    /** Скидання стану: дезінфекція + розрив прив'язки до попереднього глядача. */
    void disinfectAndReset() {
        this.assignedSeat = null;
    }

    public String getId() { return id; }
    public int getIssueCount() { return issueCount; }
    public String getAssignedSeat() { return assignedSeat; }
    public boolean isIssued() { return assignedSeat != null; }
}
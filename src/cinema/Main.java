package cinema;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        task1Singleton();
        System.out.println();
        task2Prototype();
    }

    // -------- Завдання 1: Singleton ----------
    static void task1Singleton() {
        System.out.println("=== Завдання 1. Продаж місць через різні канали (Singleton) ===");
        Hall hall2 = new Hall(2, 5, 8);
        LocalDateTime t1900 = LocalDateTime.of(2026, 11, 1, 19, 0);

        TicketSalesSystem cashierSystem = TicketSalesSystem.getInstance();
        Session evening = new Session("Дюна", hall2, t1900);
        cashierSystem.registerSession(evening);
        cashierSystem.sellSeat(Channel.CASHIER, evening.getId(), 5, 7);

        TicketSalesSystem appSystem = TicketSalesSystem.getInstance();
        appSystem.sellSeat(Channel.APP, evening.getId(), 5, 7);   // відмова

        TicketSalesSystem siteSystem = TicketSalesSystem.getInstance();

        Logger.log("Раннер", "ID системи (каса):       " + cashierSystem.getId());
        Logger.log("Раннер", "ID системи (сайт):       " + siteSystem.getId());
        Logger.log("Раннер", "ID системи (застосунок): " + appSystem.getId());
        Logger.log("Раннер", "Той самий екземпляр: "
                + (cashierSystem == siteSystem && siteSystem == appSystem));
    }

    // ---------- Завдання 2: Prototype ----------
    static void task2Prototype() {
        System.out.println("=== Завдання 2. Формування сеансів (Prototype) ===");
        Hall hall2 = new Hall(2, 5, 8);
        SessionPrototypeRegistry registry = new SessionPrototypeRegistry();

        Session proto = new Session("Дюна", hall2, LocalDateTime.of(2026, 11, 2, 10, 0));
        String key = SessionPrototypeRegistry.key("Дюна", 2);
        registry.register(key, proto);
        List<String> protoBefore = proto.describe();

        int[] hours = {12, 15, 18, 21};
        List<Session> sessions = new ArrayList<>();
        for (int h : hours) {
            sessions.add(registry.create(key, LocalDateTime.of(2026, 11, 3, h, 0)));
        }

        Session changed = sessions.get(0);
        changed.setPrice("Перший ряд", 90.0);
        changed.replaceAd(0, new AdClip("Піца «Сонце»", 25));
        changed.sell(Channel.CASHIER, 3, 4);
        changed.sell(Channel.WEBSITE, 3, 5);
        changed.sell(Channel.APP, 1, 1);
        Logger.log("Раннер", "У сеансі " + changed.getId()
                + " змінено ціну 1-го ряду, замінено ролик, продано 3 місця");

        printSideBySide("ЗРАЗОК", proto.describe(), "ЗМІНЕНИЙ СЕАНС", changed.describe());
        Logger.log("Раннер", "Зразок не змінився: " + protoBefore.equals(proto.describe()));
        Logger.log("Раннер", "Інші клони не змінилися: "
                + sessions.get(1).describe().get(1).equals(proto.describe().get(1))
                + " (ціни), "
                + sessions.get(1).describe().get(3).equals("Продані місця: —") + " (місця)");
    }

    static void printSideBySide(String lt, List<String> left, String rt, List<String> right) {
        System.out.println();
        System.out.printf("%-62s| %s%n", lt, rt);
        System.out.println("-".repeat(62) + "+" + "-".repeat(62));
        for (int i = 0; i < Math.max(left.size(), right.size()); i++) {
            String l = i < left.size() ? left.get(i) : "";
            String r = i < right.size() ? right.get(i) : "";
            System.out.printf("%-62s| %s%n", l, r);
        }
        System.out.println();
    }
}

package Java8.LocaldateTime;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class Test {
    public static void main(String[] args) {
        LocalDate today=LocalDate.now();
        System.out.println(today);
        System.out.println(today.getDayOfYear());
    }
}

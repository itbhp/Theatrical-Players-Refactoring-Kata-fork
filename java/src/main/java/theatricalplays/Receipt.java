package theatricalplays;

import java.util.List;
import java.util.function.ToIntFunction;

public record Receipt(List<Performance> performances, int totalAmounts, int totalCredits) {
    Receipt(List<Performance> performances){
        this(performances, sum(performances, Performance::amount), sum(performances, Performance::volumeCredits));
    }

    private static int sum(List<Performance> performances, ToIntFunction<Performance> amount) {
        return performances.stream().mapToInt(amount).sum();
    }

    public record Performance(String play, int seats, int amount, int volumeCredits){}
}

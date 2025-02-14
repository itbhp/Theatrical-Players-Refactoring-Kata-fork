package theatricalplays;

import java.util.List;
import java.util.function.ToIntFunction;

public record Receipt(List<PerformanceReceipt> performanceReceipts, int totalAmounts, int totalCredits) {
    Receipt(List<PerformanceReceipt> performanceReceipts){
        this(performanceReceipts, sum(performanceReceipts, PerformanceReceipt::amount), sum(performanceReceipts, PerformanceReceipt::volumeCredits));
    }

    private static int sum(List<PerformanceReceipt> performanceReceipts, ToIntFunction<PerformanceReceipt> amount) {
        return performanceReceipts.stream().mapToInt(amount).sum();
    }

    public record PerformanceReceipt(String play, int seats, int amount, int volumeCredits){}
}

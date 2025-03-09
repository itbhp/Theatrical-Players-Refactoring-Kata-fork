package theatricalplays;

import java.util.List;
import java.util.function.ToIntFunction;

public record Receipt(List<Performance> performances, int totalAmounts, int totalCredits) {
  Receipt(List<Performance> performances) {
    this(
        performances,
        sumFrom(performances, Performance::amount),
        sumFrom(performances, Performance::volumeCredits));
  }

  private static int sumFrom(List<Performance> performances, ToIntFunction<Performance> value) {
    return performances.stream().mapToInt(value).sum();
  }

  public static Receipt of(List<Performance> performances) {
    return new Receipt(performances);
  }

  public record Performance(String play, int seats, int amount, int volumeCredits) {}
}

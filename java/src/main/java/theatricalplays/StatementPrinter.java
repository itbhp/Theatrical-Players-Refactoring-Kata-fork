package theatricalplays;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

  private final NumberFormat numberFormat;
  private final Map<PlayId, Play> playsIdToPlay;

  public StatementPrinter(Map<PlayId, Play> playsIdToPlay) {
    this.playsIdToPlay = playsIdToPlay;
    this.numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
  }

  public String print(Invoice invoice) {
    var performancesOutput =
        invoice.performances().stream()
            .map(
                performance -> {
                  var play = retrievePlayBy(performance.playId());
                  var audience = new Audience(performance.audience());
                  var playAmount = play.amountFor(audience);
                  var volumeCredit = play.volumeCreditsFor(audience);
                  var message =
                      "  %s: %s (%s seats)"
                          .formatted(
                              play.name(),
                              numberFormat.format(playAmount / 100),
                              performance.audience());
                  return new PerformancesOutput(message, playAmount, volumeCredit);
                })
            .reduce(new PerformancesOutput("", 0, 0), PerformancesOutput::combine);

    return statementFrom(invoice.customer(), performancesOutput);
  }

  private String statementFrom(String customer, PerformancesOutput performancesOutput) {
    return "Statement for %s".formatted(customer)
        + "%s\n".formatted(performancesOutput.message)
        + "Amount owed is %s\n".formatted(numberFormat.format(performancesOutput.playsAmount / 100))
        + "You earned %s credits\n".formatted(performancesOutput.volumeCredits);
  }

  private Play retrievePlayBy(PlayId playId) {
    return this.playsIdToPlay.get(playId);
  }

  private record PerformancesOutput(String message, Integer playsAmount, Integer volumeCredits) {
    public PerformancesOutput combine(PerformancesOutput performancesOutput) {
      return new PerformancesOutput(
          this.message + "\n" + performancesOutput.message,
          this.playsAmount + performancesOutput.playsAmount,
          this.volumeCredits + performancesOutput.volumeCredits);
    }
  }
}

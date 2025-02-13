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
    var playsInfo =
        invoice.performances().stream()
            .map(
                performance -> {
                  var play = retrievePlayBy(performance.playId());
                  var playAmount = playAmount(performance, play);
                  var volumeCredit = volumeCredit(performance, play);
                  var message =
                      String.format(
                          "  %s: %s (%s seats)",
                          play.name(),
                          numberFormat.format(playAmount / 100),
                          performance.audience());
                  return new PerformancesOutput(message, playAmount, volumeCredit);
                })
            .reduce(new PerformancesOutput("", 0, 0), PerformancesOutput::combine);

    return statementFrom(invoice.customer(), playsInfo);
  }

  private String statementFrom(String customer, PerformancesOutput performancesOutput) {
    return String.format("Statement for %s", customer)
        + performancesOutput.message
        + "\n"
        + String.format(
            "Amount owed is %s\n", numberFormat.format(performancesOutput.playsAmount / 100))
        + String.format("You earned %s credits\n", performancesOutput.volumeCredits);
  }

  private int volumeCredit(Performance performance, Play play) {
    var volumeCredit = Math.max(performance.audience() - 30, 0);
    if ("comedy".equals(play.type())) {
      return volumeCredit + Double.valueOf((double) performance.audience() / 5).intValue();
    }
    return volumeCredit;
  }

  private int playAmount(Performance performance, Play play) {
    var thisAmount = 0;

    switch (play.type()) {
      case "tragedy":
        thisAmount = 40000;
        if (performance.audience() > 30) {
          thisAmount += 1000 * (performance.audience() - 30);
        }
        break;
      case "comedy":
        thisAmount = 30000;
        if (performance.audience() > 20) {
          thisAmount += 10000 + 500 * (performance.audience() - 20);
        }
        thisAmount += 300 * performance.audience();
        break;
      default:
        throw new Error("unknown type: ${play.type}");
    }
    return thisAmount;
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

package theatricalplays;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

  private final NumberFormat numberFormat;
  private final Map<PlayId, Play> playsIdToPlay;

  public StatementPrinter(Map<PlayId, Play> playsIdToPlay) {
    this.playsIdToPlay = playsIdToPlay;
    numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
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
                  return new Triple(message, playAmount, volumeCredit);
                })
            .reduce(new Triple("", 0, 0), Triple::combine);

    var totalAmounts = playsInfo.playAmount;
    var volumeCredits = playsInfo.volumeCredit;
    return String.format("Statement for %s", invoice.customer())
        + playsInfo.message + "\n"
        + String.format("Amount owed is %s\n", numberFormat.format(totalAmounts / 100))
        + String.format("You earned %s credits\n", volumeCredits);
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

  private record Triple(String message, Integer playAmount, Integer volumeCredit) {
    public Triple combine(Triple triple) {
      return new Triple(
          this.message + "\n" + triple.message,
          this.playAmount + triple.playAmount,
          this.volumeCredit + triple.volumeCredit);
    }
  }
}

package theatricalplays;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementPrinter {

  private final NumberFormat numberFormat;
  private final Map<PlayId, Play> playsIdToPlay;

  public StatementPrinter(Map<PlayId, Play> playsIdToPlay) {
    this.playsIdToPlay = playsIdToPlay;
    this.numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
  }

  public String print(Invoice invoice) {
    var receipt =
        new Receipt(
            invoice.performances().stream()
                .map(
                    performance -> {
                      var play = retrievePlayBy(performance.playId());
                      var audience = new Audience(performance.audience());
                      var playAmount = play.amountFor(audience);
                      var volumeCredit = play.volumeCreditsFor(audience);
                      return new Receipt.PerformanceReceipt(
                          play.name(), performance.audience(), playAmount, volumeCredit);
                    })
                .toList());

    return statementFrom(invoice.customer(), receipt);
  }

  private String statementFrom(String customer, Receipt receipt) {
    return "Statement for %s\n".formatted(customer)
        + "%s\n"
            .formatted(
                receipt.performanceReceipts().stream()
                    .map(this::performaceMessage)
                    .collect(Collectors.joining("\n")))
        + "Amount owed is %s\n".formatted(numberFormat.format(receipt.totalAmounts() / 100))
        + "You earned %s credits\n".formatted(receipt.totalCredits());
  }

  private String performaceMessage(Receipt.PerformanceReceipt performanceReceipt) {
    return "  %s: %s (%s seats)"
        .formatted(
            performanceReceipt.play(),
            numberFormat.format(performanceReceipt.amount() / 100),
            performanceReceipt.seats());
  }

  private Play retrievePlayBy(PlayId playId) {
    return this.playsIdToPlay.get(playId);
  }
}

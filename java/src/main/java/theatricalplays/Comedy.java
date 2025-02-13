package theatricalplays;

public record Comedy(String name) implements Play {
  @Override
  public int amountFor(Audience audience) {
    var amount = 30000;
    if (audience.soldTickets() > 20) {
      amount += 10000 + 500 * (audience.soldTickets() - 20);
    }
    amount += 300 * audience.soldTickets();
    return amount;
  }

  @Override
  public int volumeCreditsFor(Audience audience) {
    var base = Math.max(audience.soldTickets() - 30, 0);
    return base + Double.valueOf((double) audience.soldTickets() / 5).intValue();
  }
}

package theatricalplays;

public record Comedy(String name) implements Play {
  @Override
  public int amountFor(Audience audience) {
    var amount = 30000;
    if (audience.seats() > 20) {
      amount += 10000 + 500 * (audience.seats() - 20);
    }
    amount += 300 * audience.seats();
    return amount;
  }

  @Override
  public int volumeCreditsFor(Audience audience) {
    var base = Play.super.volumeCreditsFor(audience);
    return base + Double.valueOf((double) audience.seats() / 5).intValue();
  }
}

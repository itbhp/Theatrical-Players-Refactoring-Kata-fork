package theatricalplays;

public record Tragedy(String name) implements Play {
  @Override
  public int amountFor(Audience audience) {
    var amount = 40000;
    if (audience.seats() > 30) {
      amount += 1000 * (audience.seats() - 30);
    }
    return amount;
  }
}

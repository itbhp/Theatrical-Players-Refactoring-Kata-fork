package theatricalplays;

public record Comedy(String name, String type) implements Play {
  @Override
  public int amountFor(Audience audience) {
    var amount = 30000;
    if (audience.soldTickets() > 20) {
      amount += 10000 + 500 * (audience.soldTickets() - 20);
    }
    amount += 300 * audience.soldTickets();
    return amount;
  }
}

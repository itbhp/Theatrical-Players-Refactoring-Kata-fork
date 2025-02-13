package theatricalplays;

public record Tragedy(String name, String type) implements Play{
    @Override
    public int amountFor(Audience audience) {
        var amount = 40000;
        if (audience.soldTickets() > 30) {
            amount += 1000 * (audience.soldTickets() - 30);
        }
        return amount;
    }
}

package theatricalplays;

public record GenericPlay(String name, String type) implements Play{
    @Override
    public int amountFor(Audience audience) {
        throw new Error("unknown type: ${play.type}");
    }
}

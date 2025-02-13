package theatricalplays;

public class Performance {

    public final int audience;
    public final PlayId playId;

    public Performance(int audience, PlayId playId) {
        this.audience = audience;
        this.playId = playId;
    }
}

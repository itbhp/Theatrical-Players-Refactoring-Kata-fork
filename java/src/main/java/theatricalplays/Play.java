package theatricalplays;

public record Play(String name, String type) {
  public static Play of(String name, String type) {
    return new Play(name, type);
  }
}

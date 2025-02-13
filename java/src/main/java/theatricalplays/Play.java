package theatricalplays;

public sealed interface Play permits Tragedy, Comedy, GenericPlay {

  String name();
  String type();

  static Play of(String name, String type) {
    return switch (type) {
      case "tragedy" -> new Tragedy(name, type);
      case "comedy" -> new Comedy(name, type);
      default -> new GenericPlay(name, type);
    };
  }
}

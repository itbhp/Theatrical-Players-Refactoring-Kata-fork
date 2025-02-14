package theatricalplays;

public sealed interface Play permits Tragedy, Comedy, GenericPlay {

  String name();

  int amountFor(Audience audience);

  default int volumeCreditsFor(Audience audience){
      return Math.max(audience.seats() - 30, 0);
  }

  static Play of(String name, String type) {
    return switch (type) {
      case "tragedy" -> new Tragedy(name);
      case "comedy" -> new Comedy(name);
      default -> new GenericPlay(name, type);
    };
  }
}

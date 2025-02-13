package theatricalplays;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.approvaltests.Approvals.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatementPrinterTests {

  @Test
  void exampleStatement() {

    var hamletId = new PlayId("hamlet");
    var asLikeId = new PlayId("as-like");
    var othelloId = new PlayId("othello");

    var playsIdToPlay =
        Map.of(
            hamletId, Play.of("Hamlet", "tragedy"),
            asLikeId, Play.of("As You Like It", "comedy"),
            othelloId, Play.of("Othello", "tragedy"));

    var invoice =
        new Invoice(
            "BigCo",
            List.of(
                new Performance(hamletId, 55),
                new Performance(asLikeId, 35),
                new Performance(othelloId, 40)));

    var statementPrinter = new StatementPrinter(playsIdToPlay);
    var result = statementPrinter.print(invoice);

    verify(result);
  }

  @Test
  void statementWithNewPlayTypes() {

    var henryVId = new PlayId("henry-v");
    var asLikeId = new PlayId("as-like");
    var invoice =
        new Invoice("BigCo", List.of(new Performance(henryVId, 53), new Performance(asLikeId, 55)));
    var playsIdToPlay =
        Map.of(
            henryVId, Play.of("Henry V", "history"),
            asLikeId, Play.of("As You Like It", "pastoral"));

    var statementPrinter = new StatementPrinter(playsIdToPlay);
    assertThrows(Error.class, () -> statementPrinter.print(invoice));
  }
}

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

        var playsIdToPlay = Map.of(
                hamletId, new Play("Hamlet", "tragedy"),
                asLikeId, new Play("As You Like It", "comedy"),
                othelloId, new Play("Othello", "tragedy"));

        var invoice = new Invoice(
                "BigCo",
                List.of(
                        new Performance(55, hamletId),
                        new Performance(35, asLikeId),
                        new Performance(40, othelloId)
                )
        );

        var statementPrinter = new StatementPrinter(playsIdToPlay);
        var result = statementPrinter.print(invoice);

        verify(result);
    }

    @Test
    void statementWithNewPlayTypes() {

        var henryVId = new PlayId("henry-v");
        var asLikeId = new PlayId("as-like");
        var invoice = new Invoice(
                "BigCo",
                List.of(
                        new Performance(53, henryVId),
                        new Performance(55, asLikeId)
                )
        );
        var playsIdToPlay = Map.of(
                henryVId, new Play("Henry V", "history"),
                asLikeId, new Play("As You Like It", "pastoral"));

        var statementPrinter = new StatementPrinter(playsIdToPlay);
        assertThrows(Error.class, () -> statementPrinter.print(invoice));
    }
}

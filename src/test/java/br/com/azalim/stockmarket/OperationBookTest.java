package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.observer.impl.OperationBookObserver;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationBook;
import br.com.azalim.stockmarket.operation.info.InfoOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationBookTest {

    private static final OperationBook operationBook = new OperationBook();

    @Test
    public void testOperationRegistry() {

        assertEquals(0, operationBook.getOperations().size(), "The operation book should be empty");
        assertThrows(NullPointerException.class, () -> operationBook.register(null), "Should not accept null operations");

        InfoOperation infoOperation = mock(InfoOperation.class);

        operationBook.register(infoOperation);

        assertTrue(operationBook.getOperations().contains(infoOperation), "The operation should be registered");
        assertTrue(operationBook.getOperations(InfoOperation.class).contains(infoOperation), "The operation should be registered");

    }

    @Test
    public void testGetPriceAtInstant() {

        OfferOperation offerOperation = mock(OfferOperation.class);

        when(offerOperation.getStatus()).thenReturn(OfferOperationStatus.EXECUTED);
        when(offerOperation.getInstant()).thenReturn(Instant.ofEpochMilli(2));
        when(offerOperation.getPrice()).thenReturn(1D);

        operationBook.register(offerOperation);

        assertEquals(1, operationBook.getPriceAtInstant(Instant.ofEpochMilli(3)), "The price should be the same as the offer operation");
        assertEquals(-1, operationBook.getPriceAtInstant(Instant.ofEpochMilli(1)), "The price should be -1 if there is no operation at the given instant");

    }

    @Test
    public void testProcessOperations() {

        Operation operation = mock(Operation.class);
        AtomicBoolean processed = new AtomicBoolean(false);

        when(operation.process(any(), any())).then((Answer<Void>) invocation -> {
            processed.set(true);
            return null;
        });

        operationBook.register(operation);
        operationBook.processOperations(mock(StockMarket.class));

        assertTrue(processed.get(), "The operation should have been processed");

    }

    @Test
    public void testObservableBehaviour() {

        OperationBookObserver operationBookObserver = mock(OperationBookObserver.class);

        operationBook.observe(operationBookObserver);
        assertTrue(operationBook.getObservers().contains(operationBookObserver), "Should contain the added operation book observer");
        assertThrows(UnsupportedOperationException.class, () -> operationBook.getObservers().add(null), "Observers collection should be unmodifiable");

    }

}

package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.StockMarket;
import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationBook;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OperationTest {

    @Test
    public void testConstructor() {

        Broker broker = mock(Broker.class);
        Asset asset = mock(Asset.class);

        assertThrows(NullPointerException.class, () -> createOperation(null, asset), "Should not accept null broker");
        assertThrows(NullPointerException.class, () -> createOperation(broker, null), "Should not accept null asset");

    }

    @Test
    public void testCompareTo() {

        Operation operation = mock(Operation.class);
        Operation otherOperation = mock(Operation.class);

        when(operation.compareTo(otherOperation)).thenCallRealMethod();

        when(operation.getInstant()).thenReturn(Instant.ofEpochMilli(1));
        when(otherOperation.getInstant()).thenReturn(Instant.ofEpochMilli(2));

        assertTrue(operation.compareTo(otherOperation) < 0, "The operation should be before the other operation");

        when(otherOperation.getInstant()).thenReturn(Instant.ofEpochMilli(0));

        assertTrue(operation.compareTo(otherOperation) > 0, "The operation should be after the other operation");

    }

    public static void createOperation(Broker broker, Asset asset) {

        new Operation(broker, asset) {
            @Override
            public boolean process(StockMarket stockMarket, OperationBook operationBook) {
                return true;
            }
        };

    }

}

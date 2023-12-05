package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.AssetType;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.company.Company;
import br.com.azalim.stockmarket.operation.OperationBook;
import br.com.azalim.stockmarket.operation.info.InfoOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InfoOperationTest {

    private static Broker broker;
    private static StockMarket stockMarket;
    private static Asset asset;

    @BeforeAll
    public static void setup() {

        Company company = mock(Company.class);

        broker = mock(Broker.class);
        stockMarket = new StockMarket(Set.of(company), Set.of(broker));
        asset = new Asset(company, AssetType.COMMON, MarketType.FRACTIONAL);

    }

    @Test
    public void testConstructor() {

        assertThrows(NullPointerException.class, () -> new InfoOperation(broker, asset, null, (price) -> {}), "Should not accept null info instant");
        assertThrows(NullPointerException.class, () -> new InfoOperation(broker, asset, Instant.now(), null), "Should not accept null consumer");

    }

    @Test
    public void testProcess() {

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        InfoOperation infoOperation = new InfoOperation(broker, asset, Instant.now(), (price) -> atomicBoolean.set(true));
        OperationBook operationBook = mock(OperationBook.class);

        assertThrows(NullPointerException.class, () -> infoOperation.process(null, operationBook), "Should not accept null stock market");
        assertThrows(NullPointerException.class, () -> infoOperation.process(stockMarket, null), "Should not accept null operation book");

        when(operationBook.getPriceAtInstant(any())).thenAnswer(invocationOnMock -> 1D);

        assertFalse(infoOperation.isAnswered(), "Should not be answered");

        assertTrue(infoOperation.process(stockMarket, operationBook), "Should process successfully");

        assertTrue(atomicBoolean.get(), "Should have called the consumer");
        assertTrue(infoOperation.isAnswered(), "Should be answered");

        assertFalse(infoOperation.process(stockMarket, operationBook), "Should not process again");

    }


}

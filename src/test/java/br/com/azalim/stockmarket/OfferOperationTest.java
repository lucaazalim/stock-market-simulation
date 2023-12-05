package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.AssetType;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.wallet.Wallet;
import br.com.azalim.stockmarket.company.Company;
import br.com.azalim.stockmarket.operation.OperationBook;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperationStatus;
import br.com.azalim.stockmarket.operation.offer.OfferOperationType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferOperationTest {

    private static Broker broker, anotherBroker;
    private static StockMarket stockMarket;

    private static Asset asset;

    @BeforeAll
    public static void setup() {

        Company company = mock(Company.class);

        when(company.getSymbol()).thenReturn("FAKE");
        when(company.getAssetTypes()).thenReturn(Set.of(AssetType.COMMON));

        broker = StockMarketTest.createBroker("Broker");
        anotherBroker = StockMarketTest.createBroker("Another Broker");

        stockMarket = new StockMarket(Set.of(company), Set.of(broker));
        asset = new Asset(company, AssetType.COMMON, MarketType.FRACTIONAL);

    }

    @Test
    public void testConstructor() {

        assertThrows(NullPointerException.class, () -> new OfferOperation(broker, asset, null, 1, 1), "Should not accept null offer operation type");

        IntStream.range(-1, 1).forEach(n -> {
            assertThrows(IllegalArgumentException.class, () -> new OfferOperation(broker, asset, OfferOperationType.BUY, n, 1), "Should not accept quantity " + n);
            assertThrows(IllegalArgumentException.class, () -> new OfferOperation(broker, asset, OfferOperationType.BUY, 1, n), "Should not accept price " + n);
        });

    }

    @Test
    public void testConsumeInvalidQuantities() {

        OfferOperation offerOperation = new OfferOperation(broker, asset, OfferOperationType.BUY, 1, 1);

        IntStream.range(-1, 1).forEach(n -> assertThrows(IllegalArgumentException.class, () -> offerOperation.consumeQuantity(n), "Should not accept quantity " + n));

    }

    @Test
    public void testConsumeOnExecutedOffer() {

        OfferOperation executedOfferOperation = mock(OfferOperation.class);

        when(executedOfferOperation.getStatus()).then(invocation -> OfferOperationStatus.EXECUTED);
        when(executedOfferOperation.consumeQuantity(anyInt())).thenCallRealMethod();

        assertThrows(IllegalStateException.class, () -> executedOfferOperation.consumeQuantity(1), "Should not consume for executed offer operation");

    }

    @Test
    public void testConsumeMoreThanAvailableQuantity() {

        OfferOperation offerOperation = new OfferOperation(broker, asset, OfferOperationType.BUY, 10, 1);

        assertEquals(10, offerOperation.consumeQuantity(20), "Should consume all available quantity");

    }

    @Test
    public void testConsumeValidQuantities() {

        OfferOperation offerOperation = new OfferOperation(broker, asset, OfferOperationType.BUY, 3, 1);

        assertEquals(1, offerOperation.consumeQuantity(1), "Should consume 1 share");
        assertEquals(offerOperation.getStatus(), OfferOperationStatus.PARTIALLY_EXECUTED, "Should be partially executed");

        assertEquals(2, offerOperation.consumeQuantity(2), "Should consume quantity");
        assertEquals(offerOperation.getStatus(), OfferOperationStatus.EXECUTED, "Should be executed");

    }

    @Test
    public void testProcessWithNullOperationBook() {

        OfferOperation offerOperation = mock(OfferOperation.class);
        OperationBook operationBook = mock(OperationBook.class);

        when(offerOperation.process(any(), any())).thenCallRealMethod();

        assertThrows(NullPointerException.class, () -> offerOperation.process(null, operationBook), "Should not accept null stock market");
        assertThrows(NullPointerException.class, () -> offerOperation.process(stockMarket, null), "Should not accept null operation book");

    }

    @Test
    public void testProcessExecutedOffer() {

        OfferOperation offerOperation = mock(OfferOperation.class);
        OperationBook operationBook = mock(OperationBook.class);

        when(offerOperation.getStatus()).then(invocation -> OfferOperationStatus.EXECUTED);
        assertFalse(offerOperation.process(stockMarket, operationBook), "Should not process for executed offer operation");

    }

    @Test
    public void testProcessValidOffer() {

        OfferOperation offerOperation = new OfferOperation(broker, asset, OfferOperationType.BUY, 15, 10);
        OfferOperation matchingOfferOperation = new OfferOperation(anotherBroker, asset, OfferOperationType.SELL, 20, 5);
        OperationBook operationBook = new OperationBook();

        operationBook.register(offerOperation);
        operationBook.register(matchingOfferOperation);

        assertTrue(offerOperation.process(stockMarket, operationBook), "Should process successfully");

        assertEquals(offerOperation.getStatus(), OfferOperationStatus.EXECUTED, "Should be executed");
        assertEquals(matchingOfferOperation.getStatus(), OfferOperationStatus.PARTIALLY_EXECUTED, "Should be partially executed");

        assertEquals(15, broker.getWallet().getQuantity(asset), "Should have 15 shares");
        assertEquals(-15, anotherBroker.getWallet().getQuantity(asset), "Should have -15 shares");

    }

}

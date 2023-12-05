package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.AssetType;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.company.Company;
import br.com.azalim.stockmarket.observer.impl.TransactionObserver;
import br.com.azalim.stockmarket.operation.OperationBook;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockMarketTest {

    private static final Company company = new Company() {
        @Override
        public String getSymbol() {
            return "FAKE";
        }

        @Override
        public String getName() {
            return "Fake Company S.A.";
        }

        @Override
        public String getDescription() {
            return "Fake company description";
        }

        @Override
        public Set<AssetType> getAssetTypes() {
            return Set.of(AssetType.COMMON);
        }
    };

    private static final Broker broker = () -> "Broker";

    private static final Broker anotherBroker = () -> "Another Broker";

    private static final Broker unknownBroker = () -> "Unknown Broker";

    public static final Asset asset = new Asset(company, AssetType.COMMON, MarketType.COMMON);

    private static final Asset anotherAsset = new Asset(company, AssetType.COMMON, MarketType.FRACTIONAL);

    private static final Asset unknownAsset = new Asset(company, AssetType.UNITS, MarketType.COMMON);

    private static final StockMarket stockMarket = new StockMarket(Set.of(company), Set.of(broker, anotherBroker));

    @Test
    public void testConstructor() {

        assertThrows(NullPointerException.class, () -> new StockMarket(null, Set.of()), "Companies cannot be null");
        assertThrows(NullPointerException.class, () -> new StockMarket(Set.of(), null), "Brokers cannot be null");

    }

    @Test
    public void testGetOperationBook() {

        assertThrows(NullPointerException.class, () -> stockMarket.getOperationBook(null), "Asset cannot be null");
        assertThrows(IllegalArgumentException.class, () -> stockMarket.getOperationBook(unknownAsset), "Asset not found");
        assertNotNull(stockMarket.getOperationBook(asset), "Operation book not found");

    }

    @Test
    public void testGetOperationBooks() {

        Collection<OperationBook> operationBooks = stockMarket.getOperationBooks();

        assertEquals(2, operationBooks.size(), "Wrong number of operation books");
        assertThrows(UnsupportedOperationException.class, () -> operationBooks.add(null), "Operation books collection should be unmodifiable");

    }

    @Test
    public void testGetAssets() {

        Set<Asset> assets = stockMarket.getAssets();

        assertEquals(2, assets.size(), "Wrong number of assets");
        assertTrue(assets.contains(asset), "Assets should contain fake asset");
        assertThrows(UnsupportedOperationException.class, () -> assets.add(null), "Assets collection should be unmodifiable");

    }

    @Test
    public void testGetBrokers() {

        Set<Broker> brokers = stockMarket.getBrokers();

        assertEquals(2, brokers.size(), "Wrong number of brokers");
        assertTrue(brokers.contains(broker), "Brokers should contain fake broker");
        assertThrows(UnsupportedOperationException.class, () -> brokers.add(null), "Brokers collection should be unmodifiable");

    }

    @Test
    public void testGetWallet() {

        assertThrows(NullPointerException.class, () -> stockMarket.getWallet(null), "Should not accept null broker");
        assertThrows(IllegalArgumentException.class, () -> stockMarket.getWallet(unknownBroker), "Should not accept unknown broker");
        assertNotNull(stockMarket.getWallet(broker), "Broker not found");

    }

    @Test
    public void testRegisterTransaction() {

        OfferOperation sellOfferOperation = mock(OfferOperation.class);
        OfferOperation buyOfferOperation = mock(OfferOperation.class);

        assertThrows(NullPointerException.class, () -> stockMarket.registerTransaction(null, buyOfferOperation, 1), "Should not accept null sell offer operation");
        assertThrows(NullPointerException.class, () -> stockMarket.registerTransaction(sellOfferOperation, null, 1), "Should not accept null buy offer operation");

        when(sellOfferOperation.getAsset()).thenReturn(asset);
        when(buyOfferOperation.getAsset()).thenReturn(anotherAsset);

        when(sellOfferOperation.getBroker()).thenReturn(broker);
        when(buyOfferOperation.getBroker()).thenReturn(anotherBroker);

        assertThrows(IllegalArgumentException.class, () -> stockMarket.registerTransaction(sellOfferOperation, buyOfferOperation, 1), "Should not accept different assets");

    }

    @Test
    public void testObservableBehaviour() {

        TransactionObserver transactionObserver = mock(TransactionObserver.class);

        stockMarket.observe(transactionObserver);
        assertTrue(stockMarket.getObservers().contains(transactionObserver), "Should contain the added transaction observer");
        assertThrows(UnsupportedOperationException.class, () -> stockMarket.getObservers().add(null), "Observers collection should be unmodifiable");

    }

}

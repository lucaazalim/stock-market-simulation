package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.BrokerWallet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BrokerWalletTest {

    private static BrokerWallet brokerWallet;
    private static Asset asset;

    @BeforeAll
    public static void setup() {

        brokerWallet = new BrokerWallet();
        asset = mock(Asset.class);

    }

    @Test
    public void test() {

        when(asset.getParentAsset()).thenReturn(asset);

        assertDoesNotThrow(() -> brokerWallet.registerTransaction(asset, new Transaction(10, 5)), "Should not throw exception when crediting asset");
        assertEquals(10, brokerWallet.getQuantity(asset), "Should return 10 after crediting 10 to the asset");

        assertDoesNotThrow(() -> brokerWallet.registerTransaction(asset, new Transaction(-5, 10)), "Should not throw exception when debiting asset");
        assertEquals(5, brokerWallet.getQuantity(asset), "Should return 5 after debiting 5 from the asset");

    }

}

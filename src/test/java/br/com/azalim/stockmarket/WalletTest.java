package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.wallet.Transaction;
import br.com.azalim.stockmarket.wallet.Wallet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalletTest {

    private static Wallet wallet;
    private static Asset asset;

    @BeforeAll
    public static void setup() {

        wallet = new Wallet();
        asset = mock(Asset.class);

    }

    @Test
    public void test() {

        when(asset.getParentAsset()).thenReturn(asset);

        assertDoesNotThrow(() -> wallet.registerTransaction(asset, new Transaction(10, 5)), "Should not throw exception when crediting asset");
        assertEquals(10, wallet.getQuantity(asset), "Should return 10 after crediting 10 to the asset");

        assertDoesNotThrow(() -> wallet.registerTransaction(asset, new Transaction(-5, 10)), "Should not throw exception when debiting asset");
        assertEquals(5, wallet.getQuantity(asset), "Should return 5 after debiting 5 from the asset");

    }

}

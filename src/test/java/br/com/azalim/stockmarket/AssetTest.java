package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.AssetType;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.company.Company;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssetTest {

    @Test
    public void testConstructor() {

        Company company = mock(Company.class);

        when(company.getSymbol()).thenReturn("FAKE");

        Asset commonAsset = new Asset(company, AssetType.COMMON, MarketType.COMMON);

        assertEquals(commonAsset, commonAsset.getParentAsset(), "Should return itself as its parent asset");
        assertEquals("FAKE3", commonAsset.getSymbol(), "Should return the symbol of the asset");
        assertEquals(Objects.hash("FAKE3"), commonAsset.hashCode(), "Should return the hash code of the asset");

        Asset fractionalAsset = new Asset(commonAsset, MarketType.FRACTIONAL);

        assertEquals(commonAsset, fractionalAsset.getParentAsset(), "Should return the common asset as its parent asset");
        assertEquals("FAKE3F", fractionalAsset.getSymbol(), "Should return the symbol of the fractional asset");
        assertEquals(Objects.hash("FAKE3F"), fractionalAsset.hashCode(), "Should return the hash code of the asset");

        Asset anotherFractionalAsset = new Asset(commonAsset, MarketType.FRACTIONAL);

        assertEquals(fractionalAsset, anotherFractionalAsset, "Should return true when comparing two fractional assets with the same company, asset type and market type");
        assertNotEquals(fractionalAsset, commonAsset, "Should return false when comparing two assets with different market types");

    }

}

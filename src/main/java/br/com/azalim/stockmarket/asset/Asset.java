package br.com.azalim.stockmarket.asset;

import br.com.azalim.stockmarket.company.Company;
import static br.com.azalim.stockmarket.utils.PrintColor.*;

/**
 * Represents an asset, like PETR4, PETR4F or BOVA11.
 */
public class Asset {

    /**
     * The company that the asset is related to.
     */
    private final Company company;

    /**
     * The type of the asset.
     */
    private final AssetType assetType;

    /**
     * The market type of the asset.
     */
    private final MarketType marketType;

    /**
     * The parent asset of the asset. For example, PETR4F's parent asset is PETR4.
     */
    private final Asset parentAsset;

    /**
     * The symbol of the asset, like PETR4F or BOVA11.
     */
    private final String symbol;

    /**
     * Creates an asset.
     *
     * @param company the company that the asset is related to.
     * @param assetType the type of the asset.
     * @param marketType the market type of the asset.
     * @param parentAsset the parent asset of the asset. For example, PETR4F's parent asset should be PETR4.
     */
    public Asset(Company company, AssetType assetType, MarketType marketType, Asset parentAsset) {
        this.company = company;
        this.assetType = assetType;
        this.marketType = marketType;
        this.parentAsset = parentAsset == null ? this : parentAsset;
        this.symbol = this.company.getSymbol() + assetType.getSuffix() + marketType.getSuffix();
    }

    /**
     * @return the company that the asset is related to.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @return the type of the asset.
     */
    public AssetType getShareType() {
        return assetType;
    }

    /**
     * @return the market type of the asset.
     */
    public MarketType getMarketType() {
        return marketType;
    }

    /**
     * @return the parent asset of the asset. For example, PETR4F's parent asset is PETR4.
     */
    public Asset getParentAsset() {
        return parentAsset;
    }

    /**
     * @return the symbol of the asset, like PETR4F or BOVA11.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Compares two assets by their symbols.
     *
     * @param obj the object to be compared.
     * @return true if the symbols are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Asset asset) {
            return this.symbol.equals(asset.symbol);
        }

        return false;

    }

    /**
     * @return a colored string representing the symbol of the asset. It will be yellow.
     */
    @Override
    public String toString() {
        return ANSI_YELLOW + this.symbol + ANSI_RESET;
    }
}

package br.com.azalim.stockmarket.asset;

import br.com.azalim.stockmarket.company.Company;

import java.util.Objects;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Represents an asset, like PETR4, PETR4F or BOVA11.
 */
public class Asset {

    /**
     * The parent asset of the asset. For example, PETR4F's parent asset is PETR4.
     */
    private final Asset parentAsset;

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
     * The symbol of the asset, like PETR4F or BOVA11.
     */
    private final String symbol;

    /**
     * Creates an asset with itself as its parent asset.
     *
     * @param company    the company that the asset is related to.
     * @param assetType  the type of the asset.
     * @param marketType the market type of the asset.
     */
    public Asset(Company company, AssetType assetType, MarketType marketType) {
        this(null, company, assetType, marketType);
    }

    /**
     * Creates an asset with a given parent assent.
     *
     * @param parentAsset the parent asset of the asset. For example, PETR4F's parent asset should be PETR4.
     * @param marketType  the market type of the asset.
     */
    public Asset(Asset parentAsset, MarketType marketType) {
        this(parentAsset, parentAsset.getCompany(), parentAsset.getShareType(), marketType);
    }

    /**
     * Creates an asset with a given parent asset, company, asset type and market type.
     * This constructor is private because it should only be used by the other constructors.
     *
     * @param parentAsset the parent asset of the asset. For example, PETR4F's parent asset should be PETR4.
     * @param company     the company that the asset is related to.
     * @param assetType   the type of the asset.
     * @param marketType  the market type of the asset.
     */
    private Asset(Asset parentAsset, Company company, AssetType assetType, MarketType marketType) {
        this.parentAsset = parentAsset == null ? this : parentAsset;
        this.company = company;
        this.assetType = assetType;
        this.marketType = marketType;
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

        if (obj instanceof Asset asset) {
            return this.symbol.equals(asset.symbol);
        }

        return false;

    }

    /**
     * @return the hash code of the symbol of the asset.
     */
    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    /**
     * @return a colored string representing the symbol of the asset. It will be yellow.
     */
    @Override
    public String toString() {
        return ansi().fgYellow() + this.symbol + ansi().reset();
    }
}

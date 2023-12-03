package br.com.azalim.stockmarket.asset;

/**
 * Represents the type of an asset.
 */
public enum AssetType {

    /**
     * Assets "ordinários"
     */
    COMMON(3, true),

    /**
     * Assets "preferenciais"
     */
    PREFERRED(4, true),

    /**
     * Assets "preferenciais classe A"
     */
    PREFERRED_CLASS_A(5, true),

    /**
     * Assets "preferenciais classe B"
     */
    PREFERRED_CLASS_B(6, true),

    /**
     * Assets "BDRs, ETs e Units"
     */
    UNITS(11, false);

    /**
     * The suffix of the asset type.
     */
    private final int suffix;

    /**
     * Indicates if the asset type has a fractional market.
     */
    private final boolean fractionalMarket;

    /**
     * Creates an asset type.
     *
     * @param suffix the suffix of the asset type.
     * @param fractionalMarket indicates if the asset type has a fractional market.
     */
    AssetType(int suffix, boolean fractionalMarket) {
        this.suffix = suffix;
        this.fractionalMarket = fractionalMarket;
    }

    /**
     * @return the suffix of the asset type.
     */
    public String getSuffix() {
        return String.valueOf(this.suffix);
    }

    /**
     * @return true if the asset type has a fractional market, false otherwise.
     */
    public boolean hasFractionalMarket() {
        return this.fractionalMarket;
    }

}

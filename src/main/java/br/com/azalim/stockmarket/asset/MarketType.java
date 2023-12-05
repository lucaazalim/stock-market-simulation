package br.com.azalim.stockmarket.asset;

import java.util.function.Predicate;

/**
 * Represents the market type of an asset.
 */
public enum MarketType {

    /**
     * Assets with a common market.
     */
    COMMON("", quantity -> quantity % 100 == 0),

    /**
     * Assets with a fractional market.
     */
    FRACTIONAL("F", quantity -> quantity < 100);

    /**
     * The suffix of the market type, like "F" for the fractional market type.
     */
    private final String suffix;

    /**
     * The predicate that indicates if a quantity is valid for the market type.
     */
    private final Predicate<Integer> quantityPredicate;

    /**
     * Creates a market type.
     *
     * @param suffix            the suffix of the market type, like "F" for the fractional market type.
     * @param quantityPredicate the predicate that indicates if a quantity is valid for the market type.
     */
    MarketType(String suffix, Predicate<Integer> quantityPredicate) {
        this.suffix = suffix;
        this.quantityPredicate = quantityPredicate;
    }

    /**
     * @return the suffix of the market type, like "F" for the fractional market type.
     */
    public String getSuffix() {
        return this.suffix;
    }

    /**
     * Checks if the quantity of shares is valid for the market type.
     *
     * @param quantity the quantity of shares.
     * @return true if the quantity is valid for the market type, false otherwise.
     */
    public boolean isQuantityValid(int quantity) {
        return this.quantityPredicate.test(quantity);
    }

}

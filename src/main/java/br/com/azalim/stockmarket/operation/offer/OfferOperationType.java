package br.com.azalim.stockmarket.operation.offer;

import static br.com.azalim.stockmarket.utils.PrintColor.*;

/**
 * Represents the type of offer operation.
 */
public enum OfferOperationType {

    BUY, SELL;

    /**
     * @return a colored string representing the operation type. It will be
     * green if the operation is a buy offerand red if it is a sell offer.
     */
    @Override
    public String toString() {
        return (this == BUY ? ANSI_GREEN : ANSI_RED) + this.name() + ANSI_RESET;
    }

}

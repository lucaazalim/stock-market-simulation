package br.com.azalim.stockmarket.operation.offer;

import static org.fusesource.jansi.Ansi.*;

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
        return (this == BUY ? ansi().fgGreen() : ansi().fgRed()) + this.name() + ansi().reset();
    }

}

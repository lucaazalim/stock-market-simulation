package br.com.azalim.stockmarket.observer.impl;

import br.com.azalim.stockmarket.observer.Observer;
import br.com.azalim.stockmarket.operation.impl.OfferOperation;

/**
 * Represents an observer that observes operations.
 */
public interface OperationBookObserver extends Observer {

    /**
     * Called when a new offer operation is registered to the stock market.
     *
     * @param offerOperation the offer operation that was registered.
     */
    void onNewOfferRegistered(OfferOperation offerOperation);

}

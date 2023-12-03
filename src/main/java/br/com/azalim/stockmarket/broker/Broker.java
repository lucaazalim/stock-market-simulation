package br.com.azalim.stockmarket.broker;

import br.com.azalim.stockmarket.observer.impl.OperationBookObserver;

/**
 * Represents a broker.
 */
public interface Broker extends OperationBookObserver {

    /**
     * @return the name of the broker.
     */
    String getName();

}

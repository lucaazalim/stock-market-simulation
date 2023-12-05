package br.com.azalim.stockmarket.observer.impl;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.observer.Observer;

/**
 * Represents an observer that observes transactions.
 */
public interface TransactionObserver extends Observer {

    /**
     * Called when a new transaction is registered to the asset market.
     *
     * @param from     the broker that sold the asset.
     * @param to       the broker that bought the asset.
     * @param asset    the asset that was traded.
     * @param quantity the quantity of shares that were traded.
     * @param price    the price of each share.
     */
    void onNewTransactionRegistered(Broker from, Broker to, Asset asset, int quantity, double price);

}

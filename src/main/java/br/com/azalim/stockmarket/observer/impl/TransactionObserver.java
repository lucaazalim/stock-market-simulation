package br.com.azalim.stockmarket.observer.impl;

import br.com.azalim.stockmarket.Stock;
import br.com.azalim.stockmarket.Broker;
import br.com.azalim.stockmarket.observer.Observer;

/**
 * Represents an observer that observes transactions.
 */
public interface TransactionObserver extends Observer {

    /**
     * Called when a new transaction is registered to the stock market.
     *
     * @param from the broker that sold the stock.
     * @param to the broker that bought the stock.
     * @param stock the stock that was traded.
     * @param quantity the quantity of shares that were traded.
     * @param price the price of each share.
     */
    void onNewTransactionRegistered(Broker from, Broker to, Stock stock, int quantity, double price);

}

package br.com.azalim.stockmarket.broker;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.Transaction;

import java.util.*;

/**
 * Represents a broker wallet.
 */
public class BrokerWallet {

    /**
     * The transactions of the broker wallet.
     */
    private final Map<Asset, Set<Transaction>> transactions = new HashMap<>();

    /**
     * Registers a transaction in the broker wallet.
     *
     * @param asset the asset that the transaction is related to.
     * @param transaction the transaction to be registered.
     */
    public synchronized void registerTransaction(Asset asset, Transaction transaction) {
        asset = asset.getParentAsset(); // Make sure fractional assets are stored in the same place as their common assets
        this.transactions.computeIfAbsent(asset, key -> new TreeSet<>()).add(transaction);
    }

    /**
     * Calculates the balance of the broker wallet for the given asset.
     * It may be positive or negative.
     *
     * @param asset the asset that the transaction is related to.
     * @return the balance of the broker wallet for the given asset.
     */
    public synchronized int getBalance(Asset asset) {
        return this.transactions.get(asset).stream()
                .sorted()
                .mapToInt(Transaction::getQuantity)
                .sum();
    }

}

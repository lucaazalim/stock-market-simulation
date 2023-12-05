package br.com.azalim.stockmarket.wallet;

import br.com.azalim.stockmarket.asset.Asset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a wallet, which is a set of transactions for each asset.
 */
public class Wallet {

    /**
     * The transactions of the wallet.
     */
    private final Map<Asset, Set<Transaction>> transactions = new HashMap<>();

    /**
     * Registers a transaction in the wallet.
     *
     * @param asset       the asset that the transaction is related to.
     * @param transaction the transaction to be registered.
     */
    public synchronized void registerTransaction(Asset asset, Transaction transaction) {
        asset = asset.getParentAsset(); // Make sure fractional assets are stored in the same place as their common assets
        this.transactions.computeIfAbsent(asset, key -> new TreeSet<>()).add(transaction);
    }

    /**
     * Calculates the quantity of shares of the wallet for the given asset.
     *
     * @param asset the asset that the transaction is related to.
     * @return the quantity of shares of the wallet for the given asset.
     */
    public synchronized int getQuantity(Asset asset) {
        asset = asset.getParentAsset(); // Make sure to get the quantity of the common asset
        return this.transactions.get(asset).stream()
                .sorted()
                .mapToInt(Transaction::getQuantity)
                .sum();
    }

}

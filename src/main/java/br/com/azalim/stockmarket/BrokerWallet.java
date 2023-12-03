package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.Stock;
import br.com.azalim.stockmarket.Transaction;

import java.util.*;

/**
 * Represents a broker wallet.
 */
public class BrokerWallet {

    private final Map<Stock, Set<Transaction>> transactions = new HashMap<>();

    public void registerTransaction(Stock stock, Transaction transaction) {
        this.transactions.computeIfAbsent(stock, key -> new TreeSet<>()).add(transaction);
    }

    public int getBalance(Stock stock) {
        return this.transactions.get(stock).stream()
                .sorted()
                .mapToInt(Transaction::getQuantity)
                .sum();
    }

}

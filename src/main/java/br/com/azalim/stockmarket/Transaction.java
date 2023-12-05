package br.com.azalim.stockmarket;

import java.time.Instant;

/**
 * Represents a credit or debit of shares in the broker wallet.
 */
public class Transaction implements Comparable<Transaction> {

    /**
     * The quantity of shares in the transaction.
     */
    private final int quantity;

    /**
     * The price of each share in the transaction.
     */
    private final double price;

    /**
     * The instant of the transaction.
     */
    private final Instant instant;

    /**
     * Creates a new transaction with the given quantity and price.
     *
     * @param quantity the quantity of shares in the transaction.
     * @param price    the price of the shares in the transaction.
     */
    public Transaction(int quantity, double price) {

        if (quantity == 0) {
            throw new IllegalArgumentException("The quantity must be different than zero");
        }

        if (price < 0) {
            throw new IllegalArgumentException("The price must be greater than or equal to zero");
        }

        this.quantity = quantity;
        this.price = price;
        this.instant = Instant.now();

    }

    /**
     * @return the quantity of shares in the transaction.
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * @return the price of each share in the transaction.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * @return the total value of the transaction (quantity * price).
     */
    public double getTotalValue() {
        return this.quantity * this.price;
    }

    /**
     * @return the instant of the transaction.
     */
    public Instant getInstant() {
        return this.instant;
    }

    /**
     * Compares this transaction with another transaction based on the instant of the transaction.
     *
     * @param other the other transaction to be compared.
     * @return a negative integer, zero, or a positive integer as this transaction is before, at the same instant, or after the other transaction.
     */
    @Override
    public int compareTo(Transaction other) {
        return this.getInstant().compareTo(other.getInstant());
    }

}

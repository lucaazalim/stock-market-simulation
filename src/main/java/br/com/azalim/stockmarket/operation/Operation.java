package br.com.azalim.stockmarket.operation;

import br.com.azalim.stockmarket.Broker;
import br.com.azalim.stockmarket.Stock;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents an operation. It can be a buy or sell offer ({@link br.com.azalim.stockmarket.operation.impl.OfferOperation})
 * or a request for stock price information ({@link br.com.azalim.stockmarket.operation.impl.InfoOperation}).
 */
public abstract class Operation implements Comparable<Operation> {

    /**
     * The broker that made the operation.
     */
    private final Broker broker;

    /**
     * The stock that the operation is related to.
     */
    private final Stock stock;

    /**
     * The instant that the operation was created.
     */
    private final Instant instant;

    /**
     * Creates an operation.
     *
     * @param broker the broker that owns the operation.
     * @param stock the stock that the operation is related to.
     */
    public Operation(Broker broker, Stock stock) {

        Objects.requireNonNull(broker);
        Objects.requireNonNull(stock);

        this.broker = broker;
        this.stock = stock;
        this.instant = Instant.now();

    }

    /**
     * @return the broker that owns the operation.
     */
    public Broker getBroker() {
        return this.broker;
    }

    /**
     * @return the stock that the operation is related to.
     */
    public Stock getStock() {
        return this.stock;
    }

    /**
     * @return the instant that the operation was created.
     */
    public Instant getInstant() {
        return instant;
    }

    /**
     * Processes the operation.
     */
    public abstract void process();

    /**
     * Compares this operation with another operation.
     * The comparison is made by the instant that the operations were created.
     *
     * @param other the other operation to be compared.
     * @return the result of the comparison.
     */
    @Override
    public int compareTo(Operation other) {
        return this.getInstant().compareTo(other.getInstant());
    }

}

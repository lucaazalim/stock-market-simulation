package br.com.azalim.stockmarket.operation;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents an operation. It can be a buy or sell offer ({@link OfferOperation})
 * or a request for asset price information ({@link br.com.azalim.stockmarket.operation.info.InfoOperation}).
 */
public abstract class Operation implements Comparable<Operation> {

    /**
     * The broker that made the operation.
     */
    private final Broker broker;

    /**
     * The asset that the operation is related to.
     */
    private final Asset asset;

    /**
     * The instant that the operation was created.
     */
    private final Instant instant;

    /**
     * Creates an operation.
     *
     * @param broker the broker that owns the operation.
     * @param asset the asset that the operation is related to.
     */
    public Operation(Broker broker, Asset asset) {

        Objects.requireNonNull(broker);
        Objects.requireNonNull(asset);

        this.broker = broker;
        this.asset = asset;
        this.instant = Instant.now();

    }

    /**
     * @return the broker that owns the operation.
     */
    public Broker getBroker() {
        return this.broker;
    }

    /**
     * @return the asset that the operation is related to.
     */
    public Asset getStock() {
        return this.asset;
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

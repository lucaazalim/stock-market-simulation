package br.com.azalim.stockmarket.operation.offer;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.StockMarket;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.Operation;

import java.util.Objects;

/**
 * Represents a buy or sell offer.
 */
public class OfferOperation extends Operation {

    /**
     * The type of the operation.
     */
    private final OfferOperationType type;

    /**
     * The quantity of shares that are being offered.
     */
    private int quantity;

    /**
     * The price of each share.
     */
    private final double price;

    /**
     * The status of the offer.
     */
    private OfferOperationStatus status;

    /**
     * Creates an offer operation.
     *
     * @param broker   the broker that owns the operation.
     * @param asset    the asset that the operation is related to.
     * @param type     the type of the operation.
     * @param quantity the quantity of shares that are being offered.
     * @param price    the price of each share.
     */
    public OfferOperation(Broker broker, Asset asset, OfferOperationType type, int quantity, double price) {

        super(broker, asset);

        if(!asset.getMarketType().isQuantityValid(quantity)) {
            throw new IllegalArgumentException("Invalid quantity for the market type");
        }

        Objects.requireNonNull(type);

        if (!(type == OfferOperationType.BUY || type == OfferOperationType.SELL)) {
            throw new IllegalArgumentException("Invalid operation type");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.status = OfferOperationStatus.OPEN;

    }

    /**
     * @return the type of the operation.
     */
    public OfferOperationType getType() {
        return type;
    }

    /**
     * @return the quantity of shares that are being offered.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Consumes a given quantity of shares from the offer.
     *
     * @param quantity the quantity of shares to be consumed.
     * @return the quantity of shares that were consumed.
     */
    public int consumeQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity to consume: " + quantity);
        }

        if (status == OfferOperationStatus.EXECUTED) {
            throw new IllegalStateException("Cannot consume quantity for an executed offer");
        }

        int consumedQuantity = Math.min(this.quantity, quantity);
        this.quantity -= consumedQuantity;

        this.status = this.quantity == 0 ? OfferOperationStatus.EXECUTED : OfferOperationStatus.PARTIALLY_EXECUTED;
        return consumedQuantity;

    }

    /**
     * @return the price of each share.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the status of the offer.
     */
    public OfferOperationStatus getStatus() {
        return status;
    }

    /**
     * Checks if a given offer is a possible candidate to be executed against this offer.
     *
     * @param offerOperation the offer operation to be checked.
     * @return true if the offer matches the given offer operation, false otherwise.
     */
    public boolean matches(OfferOperation offerOperation) {

        Objects.requireNonNull(offerOperation);

        return this != offerOperation
                && this.getType() != offerOperation.getType()
                && offerOperation.getStatus() != OfferOperationStatus.EXECUTED
                && (this.getType() == OfferOperationType.BUY ? this.getPrice() >= offerOperation.getPrice() : this.getPrice() <= offerOperation.getPrice());

    }

    /**
     * Registers a transaction between this offer and a given offer operation.
     *
     * @param candidateOfferOperation the offer operation to be used to create the transaction.
     * @param consumedQuantity        the quantity of shares that were consumed from the offer operation.
     */
    private void registerTransaction(OfferOperation candidateOfferOperation, int consumedQuantity) {

        Broker from, to;
        double price;

        if (this.getType() == OfferOperationType.BUY) {
            from = candidateOfferOperation.getBroker();
            to = this.getBroker();
            price = candidateOfferOperation.getPrice();
        } else {
            from = this.getBroker();
            to = candidateOfferOperation.getBroker();
            price = this.getPrice();
        }

        StockMarket.getInstance().registerTransaction(from, to, this.getStock(), consumedQuantity, price);

    }

    /**
     * Processes the offer, executing it against other offers if possible.
     */
    @Override
    public void process() {

        if (this.getStatus() == OfferOperationStatus.EXECUTED) {
            return;
        }

        StockMarket.getInstance().getOperationBook(this.getStock()).getOperations(OfferOperation.class)
                .stream()
                .filter(this::matches)
                .forEach(offerOperation -> {

                    // The if statement below may not be a filter of the stream above
                    // because the offer status may be changed by the code inside it.
                    if (this.getStatus() != OfferOperationStatus.EXECUTED) {

                        int consumedQuantity = offerOperation.consumeQuantity(this.getQuantity());

                        this.consumeQuantity(consumedQuantity);
                        this.registerTransaction(offerOperation, consumedQuantity);

                    }

                });

    }

}

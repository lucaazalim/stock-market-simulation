package br.com.azalim.stockmarket.operation.offer;

import br.com.azalim.stockmarket.StockMarket;
import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationBook;

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

        if (!asset.getMarketType().isQuantityValid(quantity)) {
            throw new IllegalArgumentException("Invalid quantity for the asset market type");
        }

        Objects.requireNonNull(type);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0: " + quantity);
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0: " + price);
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
     * Consumes a given quantity of shares from the offer.
     *
     * @param quantity the quantity of shares to be consumed.
     * @return the quantity of shares that were consumed.
     */
    public int consumeQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity to consume: " + quantity);
        }

        if (this.getStatus() == OfferOperationStatus.EXECUTED) {
            throw new IllegalStateException("Cannot consume quantity for an executed offer");
        }

        int consumedQuantity = Math.min(this.quantity, quantity);
        this.quantity -= consumedQuantity;

        this.status = this.quantity == 0 ? OfferOperationStatus.EXECUTED : OfferOperationStatus.PARTIALLY_EXECUTED;
        return consumedQuantity;

    }

    /**
     * Checks if a given offer is a possible candidate to be executed against this offer.
     *
     * @param offerOperation the offer operation to be checked.
     * @return true if the offer matches the given offer operation, false otherwise.
     */
    private boolean matches(OfferOperation offerOperation) {

        Objects.requireNonNull(offerOperation);

        return this != offerOperation
                && this.getType() != offerOperation.getType()
                && offerOperation.getStatus() != OfferOperationStatus.EXECUTED
                && (this.getType() == OfferOperationType.BUY ? this.getPrice() >= offerOperation.getPrice() : this.getPrice() <= offerOperation.getPrice());

    }

    /**
     * Processes the offer, executing it against other offers if possible.
     *
     * @param stockMarket the stock market where the offer is being processed.
     */
    @Override
    public boolean process(StockMarket stockMarket, OperationBook operationBook) {

        Objects.requireNonNull(stockMarket);
        Objects.requireNonNull(operationBook);

        if (this.getStatus() == OfferOperationStatus.EXECUTED) {
            return false;
        }

        operationBook.getOperations(OfferOperation.class)
                .stream()
                .filter(this::matches)
                .forEach(offerOperation -> {

                    // The if statement below may not be a filter of the stream above
                    // because the offer status may be changed by the code inside it.
                    if (this.getStatus() != OfferOperationStatus.EXECUTED) {

                        int consumedQuantity = offerOperation.consumeQuantity(this.getQuantity());
                        this.consumeQuantity(consumedQuantity);

                        OfferOperation sellOfferOperation = this.getType() == OfferOperationType.SELL ? this : offerOperation;
                        OfferOperation buyOfferOperation = this.getType() == OfferOperationType.BUY ? this : offerOperation;

                        stockMarket.registerTransaction(sellOfferOperation, buyOfferOperation, consumedQuantity);

                    }

                });

        return true;

    }

}

package br.com.azalim.stockmarket.operation;

import br.com.azalim.stockmarket.observer.Observable;
import br.com.azalim.stockmarket.observer.impl.OperationBookObserver;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperationStatus;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * Represents the operation book of a stock.
 */
public class OperationBook implements Observable<OperationBookObserver> {

    /**
     * The operations registered to the book.
     */
    private final Set<Operation> operations = new ConcurrentSkipListSet<>();

    /**
     * The observers that are going to be notified when there is a new offer operation registered.
     */
    private final Set<OperationBookObserver> observers = new HashSet<>();

    /**
     * @return the operations registered to the book.
     */
    public Set<Operation> getOperations() {
        return this.operations;
    }

    /**
     * Finds all the operations that are instances of a given class.
     *
     * @param operationClass the class of the operations to be found.
     * @return the operations that are instances of the given class.
     * @param <T> the type of the operations to be found.
     */
    public <T> Set<T> getOperations(Class<T> operationClass) {
        return this.operations.stream()
                .filter(operationClass::isInstance)
                .map(operationClass::cast)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Registers a new operation to the book.
     * If the operation is an offer operation, the observers will be notified.
     *
     * @param operation the operation to be registered.
     */
    public void register(Operation operation) {

        Objects.requireNonNull(operation);

        this.operations.add(operation);

        if (operation instanceof OfferOperation offerOperation) {
            this.observers.forEach(operationBookObserver -> operationBookObserver.onNewOfferRegistered(offerOperation));
        }

    }

    /**
     * Retrieves the price of the last executed offer operation before a given instant.
     * If there is no executed offer operation before the given instant, -1 will be returned.
     *
     * @param instant the instant to be used to find the last executed offer operation.
     * @return the price of the last executed offer operation before the given instant.
     */
    public double getPriceAtInstant(Instant instant) {

        return this.getOperations(OfferOperation.class)
                .stream()
                .filter(offerOperation -> offerOperation.getStatus() == OfferOperationStatus.EXECUTED || offerOperation.getStatus() == OfferOperationStatus.PARTIALLY_EXECUTED)
                .filter(offerOperation -> offerOperation.getInstant().isBefore(instant))
                .sorted(Comparator.reverseOrder())
                .limit(1)
                .map(OfferOperation::getPrice)
                .findFirst().orElse(-1D);

    }

    /**
     * Subscribes to be notified of new offer operations registered to the book.
     *
     * @param observer the observer that is going to be notified when new offer operations are registered.
     */
    @Override
    public void observe(OperationBookObserver observer) {
        this.observers.add(observer);
    }

}

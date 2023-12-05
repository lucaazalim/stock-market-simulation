package br.com.azalim.stockmarket.observer;

import java.util.Collection;

/**
 * Represents an observable object.
 *
 * @param <T> the type of the observers.
 */
public interface Observable<T extends Observer> {

    /**
     * Adds an observer to the observable object.
     *
     * @param observer the observer to be added.
     */
    void observe(T observer);

    /**
     * @return the observers of the observable object. It is usually a copy or an unmodifiable collection.
     */
    Collection<T> getObservers();

}

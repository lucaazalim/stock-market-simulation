package br.com.azalim.stockmarket.observer;

/**
 * Represents an observable object.
 * @param <T> the type of the observers.
 */
public interface Observable<T extends Observer> {

    void observe(T observer);

}

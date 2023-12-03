package br.com.azalim.stockmarket.broker;

import br.com.azalim.stockmarket.utils.PrintColor;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;

/**
 * Represents some of the B3 brokers.
 */
public enum SampleBroker implements Broker {

    XPIN("XP Investimentos"),
    CCLR("Clear Corretora"),
    RICO("Rico Investimentos"),
    EASY("Easynvest"),
    MODA("Modalmais"),
    ITAU("Itaú Corretora"),
    BRAD("Bradesco Corretora"),
    GENA("Genial Investimentos"),
    ATIV("Ativa Investimentos"),
    MIRA("Mirae Asset");

    /**
     * The name of the broker.
     */
    private final String name;

    /**
     * Creates a broker.
     *
     * @param name the name of the broker.
     */
    SampleBroker(String name) {
        this.name = name;
    }

    /**
     * @return the name of the broker.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Called when a new offer operation is registered to the stock market.
     * Prints a message to the console.
     *
     * @param offerOperation the offer operation that was registered.
     */
    @Override
    public void onNewOfferRegistered(OfferOperation offerOperation) {
        System.out.println(this + " observes " + offerOperation.getStock() + " and was notified of a new offer.");
    }

    /**
     * @return a colored string with the acronym of the broker. It will be blue.
     */
    @Override
    public String toString() {
        return PrintColor.ANSI_BLUE + this.name() + PrintColor.ANSI_RESET;
    }

}

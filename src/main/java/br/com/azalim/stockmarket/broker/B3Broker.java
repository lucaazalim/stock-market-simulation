package br.com.azalim.stockmarket.broker;

import br.com.azalim.stockmarket.observer.impl.OperationBookObserver;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import br.com.azalim.stockmarket.wallet.Wallet;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Represents some of the B3 brokers.
 */
public enum B3Broker implements Broker, OperationBookObserver {

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

    private final Wallet wallet = new Wallet();

    /**
     * Creates a broker.
     *
     * @param name the name of the broker.
     */
    B3Broker(String name) {
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
     * @return the wallet of the broker.
     */
    @Override
    public Wallet getWallet() {
        return this.wallet;
    }

    /**
     * Called when a new offer operation is registered to the stock market.
     * Prints a message to the console.
     *
     * @param offerOperation the offer operation that was registered.
     */
    @Override
    public void onNewOfferRegistered(OfferOperation offerOperation) {
        System.out.println(ansi().fgCyan().a("[Broker] ").a(this).a(" observes ")
                .a(offerOperation.getAsset()).a(" and was notified of a new offer."));
    }

    /**
     * @return a colored string with the acronym of the broker. It will be blue.
     */
    @Override
    public String toString() {
        return ansi().fgBlue() + this.name() + ansi().reset();
    }

}

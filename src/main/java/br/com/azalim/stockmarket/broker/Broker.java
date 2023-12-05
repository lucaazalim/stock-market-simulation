package br.com.azalim.stockmarket.broker;

import br.com.azalim.stockmarket.wallet.Wallet;

/**
 * Represents a broker.
 */
public interface Broker {

    /**
     * @return the name of the broker.
     */
    String getName();

    /**
     * @return the wallet of the broker.
     */
    Wallet getWallet();

}

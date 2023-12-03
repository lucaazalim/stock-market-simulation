package br.com.azalim.stockmarket.operation;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.info.InfoOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperationType;

import java.time.Instant;
import java.util.function.Consumer;

public class OperationFactory {

    /**
     * Creates an offer operation.
     * {@link OfferOperation}
     */
    public static OfferOperation createOfferOperation(Broker broker, Asset asset, OfferOperationType type, int quantity, double price) {
        return new OfferOperation(broker, asset, type, quantity, price);
    }

    /**
     * Creates an info operation.
     * {@link InfoOperation}
     */
    public static InfoOperation createInfoOperation(Broker broker, Asset asset, Instant infoInstant, Consumer<Double> answerConsumer) {
        return new InfoOperation(broker, asset, infoInstant, answerConsumer);
    }

}

package br.com.azalim.stockmarket.operation;

import br.com.azalim.stockmarket.Broker;
import br.com.azalim.stockmarket.operation.enums.OfferOperationType;
import br.com.azalim.stockmarket.operation.impl.InfoOperation;
import br.com.azalim.stockmarket.operation.impl.OfferOperation;
import br.com.azalim.stockmarket.Stock;

import java.time.Instant;
import java.util.function.Consumer;

public class OperationFactory {

    /**
     * Creates an offer operation.
     * {@link OfferOperation}
     */
    public static OfferOperation createOfferOperation(Broker broker, Stock stock, OfferOperationType type, int quantity, double price) {
        return new OfferOperation(broker, stock, type, quantity, price);
    }

    /**
     * Creates an info operation.
     * {@link InfoOperation}
     */
    public static InfoOperation createInfoOperation(Broker broker, Stock stock, Instant infoInstant, Consumer<Double> answerConsumer) {
        return new InfoOperation(broker, stock, infoInstant, answerConsumer);
    }

}

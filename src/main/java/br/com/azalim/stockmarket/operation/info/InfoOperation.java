package br.com.azalim.stockmarket.operation.info;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.StockMarket;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationBook;

import java.time.Instant;
import java.util.function.Consumer;

/**
 * Represents a request for stock price information.
 */
public class InfoOperation extends Operation {

    /**
     * The instant of the price information you want.
     */
    private final Instant infoInstant;

    /**
     * The consumer that will receive the price information.
     */
    private final Consumer<Double> answerConsumer;

    /**
     * Indicates if the operation has been answered.
     */
    private boolean answered = false;

    /**
     * Creates an info operation.
     *
     * @param broker the broker that owns the request.
     * @param asset the asset that the request is related to.
     * @param infoInstant the instant of the price information you want.
     * @param answerConsumer the consumer that will receive the price information.
     */
    public InfoOperation(Broker broker, Asset asset, Instant infoInstant, Consumer<Double> answerConsumer) {
        super(broker, asset);
        this.infoInstant = infoInstant;
        this.answerConsumer = answerConsumer;
    }

    /**
     * @return the instant of the price information you want.
     */
    public Instant getInfoInstant() {
        return this.infoInstant;
    }

    /**
     * @return the consumer that will receive the price information.
     */
    public boolean isAnswered() {
        return this.answered;
    }

    /**
     * Answers the request by sending the price information to the consumer.
     *
     * @param value the price information.
     */
    public void answer(double value) {
        this.answerConsumer.accept(value);
        this.answered = true;
    }

    /**
     * Processes the operation by answering to the request with the proper price information.
     */
    @Override
    public void process() {

        if (!this.isAnswered()) {
            OperationBook operationBook = StockMarket.getInstance().getOperationBook(this.getStock());
            this.answer(operationBook.getPriceAtInstant(this.getInfoInstant()));
        }

    }
}

package br.com.azalim.stockmarket.operation.info;

import br.com.azalim.stockmarket.StockMarket;
import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationBook;

import java.time.Instant;
import java.util.Objects;
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
     * @param broker         the broker that owns the request.
     * @param asset          the asset that the request is related to.
     * @param infoInstant    the instant of the price information you want.
     * @param answerConsumer the consumer that will receive the price information.
     */
    public InfoOperation(Broker broker, Asset asset, Instant infoInstant, Consumer<Double> answerConsumer) {

        super(broker, asset);

        Objects.requireNonNull(infoInstant);
        Objects.requireNonNull(answerConsumer);

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
     * Processes the operation by answering to the request with the proper price information.
     *
     * @param stockMarket the stock market that contains the operation books.
     */
    @Override
    public boolean process(StockMarket stockMarket, OperationBook operationBook) {

        Objects.requireNonNull(stockMarket);
        Objects.requireNonNull(operationBook);

        if (!this.isAnswered()) {

            this.answerConsumer.accept(operationBook.getPriceAtInstant(this.getInfoInstant()));
            this.answered = true;

            return true;

        }

        return false;

    }
}

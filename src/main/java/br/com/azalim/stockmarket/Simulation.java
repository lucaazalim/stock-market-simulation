package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.operation.enums.OfferOperationType;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationFactory;
import br.com.azalim.stockmarket.operation.impl.InfoOperation;
import br.com.azalim.stockmarket.operation.impl.OfferOperation;

import java.time.Instant;
import java.util.*;

import static br.com.azalim.stockmarket.PrintColor.*;

/**
 * Represents a simulation of the stock market.
 * It creates one thread for each broker, observes random stocks and registers random operations.
 */
public class Simulation {

    private static final Random RANDOM = new Random();

    /**
     * Starts the simulation.
     */
    public static void start() {

        observeTransactions();

        for (Broker broker : Broker.values()) {

            Thread thread = new Thread(() -> {

                observeRandomStocks(broker);

                while (true) {

                    try {
                        Thread.sleep(RANDOM.nextLong(300, 10000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    registerRandomOperation(broker);

                }

            });

            thread.start();

        }

    }

    /**
     * Observes the creation of new transactions and prints information about it.
     */
    private static void observeTransactions() {
        StockMarket.getInstance().observe((from, to, stock, quantity, price) ->
                System.out.println(ANSI_GREEN + "New transaction! " + ANSI_RESET + quantity + " " + stock
                        + " were transfered from " + from + " to " + to + ".")
        );
    }

    /**
     * Observes a random number of random stocks.
     * @param broker the broker that will observe the stocks.
     */
    private static void observeRandomStocks(Broker broker) {

        List<Stock> allStocks = Arrays.asList(Stock.values());
        int stocksToObserve = RANDOM.nextInt(allStocks.size());

        Collections.shuffle(allStocks);

        allStocks.stream().limit(stocksToObserve).forEach(stock -> StockMarket.getInstance().getOperationBook(stock).observe(broker));

    }

    /**
     * Registers a random operation. It can be an info operation or an offer operation.
     * @param broker the broker that will register the operation.
     */
    private static void registerRandomOperation(Broker broker) {

        Stock randomStock = Stock.values()[RANDOM.nextInt(Stock.values().length)];
        Operation randomOperation;

        if (RANDOM.nextDouble() < 0.2D) {

            InfoOperation infoOperation = createRandomInfoOperation(broker, randomStock);
            randomOperation = infoOperation;

            System.out.println(ANSI_GREEN + "New price request! " + ANSI_RESET + broker + " wants to know "
                    + infoOperation.getStock() + "'s price.");

        } else {

            OfferOperation offerOperation = createRandomOfferOperation(broker, randomStock);
            randomOperation = offerOperation;

            System.out.println(ANSI_GREEN + "New offer! " + ANSI_RESET + broker + " wants to buy "
                    + offerOperation.getQuantity() + " " + offerOperation.getStock() + " for "
                    + offerOperation.getPrice() + " each.");

        }

        StockMarket.getInstance().registerOperation(randomOperation);

    }

    /**
     * Creates a random info operation.
     *
     * @param broker the broker that will own the operation.
     * @param stock the stock of the info operation to be created.
     * @return the created info operation.
     */
    private static InfoOperation createRandomInfoOperation(Broker broker, Stock stock) {

        Instant randomRecentInstant = Instant.now().minusSeconds(RANDOM.nextInt(10));

        return OperationFactory.createInfoOperation(
                broker, stock, randomRecentInstant,
                price -> {
                    String displayPrice = (price < 0 ? ANSI_RED + "[no executed offers yet]" : price) + ANSI_RESET;
                    System.out.println(broker + " requested the price of " + stock + " at " + randomRecentInstant + " and was answered with " + displayPrice + ".");
                }
        );

    }

    /**
     * Creates a random offer operation.
     *
     * @param broker the broker that will own the operation.
     * @param stock the stock of the offer operation to be created.
     * @return the created offer operation.
     */
    private static OfferOperation createRandomOfferOperation(Broker broker, Stock stock) {

        OfferOperationType randomOfferOperationType = OfferOperationType.values()[RANDOM.nextInt(OfferOperationType.values().length)];
        int randomQuantity = RANDOM.nextInt(25, 250); // random quantity from 25 to 249
        double randomPrice = ((int) (RANDOM.nextDouble() * 10000)) / 100D; // random double value from 0 to 100 with two decimal cases

        return OperationFactory.createOfferOperation(
                broker, stock, randomOfferOperationType, randomQuantity, randomPrice
        );

    }

}

package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.observer.impl.OperationBookObserver;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationFactory;
import br.com.azalim.stockmarket.operation.info.InfoOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperationType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Represents a simulation of the stock market.
 * It creates one thread for each broker, observes random stocks and registers random operations.
 */
public class Simulation {

    private static final Random RANDOM = new Random();

    private static boolean RUNNING = false;

    /**
     * Starts the simulation.
     */
    public static void start() {

        if (RUNNING) {
            throw new IllegalStateException("Simulation is already running");
        }

        RUNNING = true;

        observeTransactions();

        StockMarket.getInstance().getBrokers().forEach(broker -> {

            if (broker instanceof OperationBookObserver operationBookObserver) {
                observeRandomStocks(operationBookObserver);
            }

            Thread thread = new Thread(() -> {

                try {

                    while (true) {

                        try {
                            Thread.sleep(RANDOM.nextLong(300, 10000));
                        } catch (InterruptedException ignored) {
                        }

                        registerRandomOperation(broker);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            thread.setName("Broker Thread - " + broker);
            thread.start();

        });

    }

    public static void stop() {

        if (!RUNNING) {
            throw new IllegalStateException("Simulation is not running");
        }

        // TODO as a future improvement, we could print a summary of the simulation here

        RUNNING = false;

    }

    /**
     * Observes the creation of new transactions and prints information about it.
     */
    private static void observeTransactions() {
        StockMarket.getInstance().observe((from, to, stock, quantity, price) ->
                log(ansi().fgGreen().a("New transaction! ").reset().a(quantity).a(" shares of ").a(stock)
                        .a(" were transfered from ").a(from).a(" to ").a(to).a("."))
        );
    }

    /**
     * Observes a random number of random stocks.
     *
     * @param operationBookObserver the observer that will observe the operation books.
     */
    private static void observeRandomStocks(OperationBookObserver operationBookObserver) {

        List<Asset> allAssets = new ArrayList<>(StockMarket.getInstance().getOperationBooks().keySet());

        String stocksBeingObserved = allAssets.stream()
                .skip(RANDOM.nextInt(allAssets.size()))
                .peek(stock -> StockMarket.getInstance().getOperationBook(stock).observe(operationBookObserver))
                .map(Asset::toString)
                .collect(Collectors.joining(", "));

        log(ansi().a(operationBookObserver).a(" is observing the following stocks: ").a(stocksBeingObserved).a("."));

    }

    /**
     * Registers a random operation. It can be an info operation or an offer operation.
     *
     * @param broker the broker that will register the operation.
     */
    private static void registerRandomOperation(Broker broker) {

        Set<Asset> allAssets = StockMarket.getInstance().getOperationBooks().keySet();
        Asset randomAsset = allAssets.stream().skip(RANDOM.nextInt(allAssets.size())).findFirst().orElseThrow();
        Operation randomOperation;

        if (RANDOM.nextDouble() < 0.2D) {

            InfoOperation infoOperation = createRandomInfoOperation(broker, randomAsset);
            randomOperation = infoOperation;

            log(ansi().fgGreen().a("New price request! ").reset().a(broker).a(" wants to know ")
                    .a(infoOperation.getAsset()).a("'s price."));

        } else {

            OfferOperation offerOperation = createRandomOfferOperation(broker, randomAsset);
            randomOperation = offerOperation;

            log(ansi().fgGreen().a("New offer! ").reset().a(broker).a(" wants to ")
                    .a(offerOperation.getType() == OfferOperationType.BUY ? "buy " : "sell ")
                    .a(offerOperation.getQuantity()).a(" shares of ").a(offerOperation.getAsset())
                    .a(" for ").a(offerOperation.getPrice()).a(" each."));

        }

        StockMarket.getInstance().getOperationBook(randomAsset).register(randomOperation);

    }

    /**
     * Creates a random info operation.
     *
     * @param broker the broker that will own the operation.
     * @param asset  the asset of the info operation to be created.
     * @return the created info operation.
     */
    private static InfoOperation createRandomInfoOperation(Broker broker, Asset asset) {

        Instant randomRecentInstant = Instant.now().minusSeconds(RANDOM.nextInt(10));

        return OperationFactory.createInfoOperation(
                broker, asset, randomRecentInstant,
                price -> {

                    LocalDateTime localDateTime = LocalDateTime.ofInstant(randomRecentInstant, ZoneId.systemDefault());
                    String displayInstant = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS").format(localDateTime);
                    String displayPrice = (price < 0 ? ansi().fgRed().a("[no price]") : price).toString();

                    log(ansi().a(broker).a(" requested the price of ").a(asset).a(" at ")
                            .a(displayInstant).a(" and received ").a(displayPrice).reset().a("."));

                }
        );

    }

    /**
     * Creates a random offer operation.
     *
     * @param broker the broker that will own the operation.
     * @param asset  the asset of the offer operation to be created.
     * @return the created offer operation.
     */
    private static OfferOperation createRandomOfferOperation(Broker broker, Asset asset) {

        OfferOperationType randomOfferOperationType = OfferOperationType.values()[RANDOM.nextInt(OfferOperationType.values().length)];
        int randomQuantity = asset.getMarketType() == MarketType.COMMON
                ? RANDOM.nextInt(1, 16) * 100 // random multiple of 100 from 100 to 1500
                : RANDOM.nextInt(1, 100); // random integer value from 1 to 99
        double randomPrice = RANDOM.nextInt(1, 10001) / 100D; // random double value from 1 to 100 with two decimal cases

        return OperationFactory.createOfferOperation(
                broker, asset, randomOfferOperationType, randomQuantity, randomPrice
        );

    }

    private static void log(Object message) {
        System.out.println(ansi().fgCyan().a("[Simulation] ").reset().a(message).reset());
    }

}

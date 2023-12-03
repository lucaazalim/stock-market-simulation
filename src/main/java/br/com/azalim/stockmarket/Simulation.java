package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.broker.SampleBroker;
import br.com.azalim.stockmarket.operation.offer.OfferOperationType;
import br.com.azalim.stockmarket.operation.Operation;
import br.com.azalim.stockmarket.operation.OperationFactory;
import br.com.azalim.stockmarket.operation.info.InfoOperation;
import br.com.azalim.stockmarket.operation.offer.OfferOperation;

import java.time.Instant;
import java.util.*;

import static br.com.azalim.stockmarket.utils.PrintColor.*;

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

        for (SampleBroker broker : SampleBroker.values()) {

            Thread thread = new Thread(() -> {

                try {

                    observeRandomStocks(broker);

                    while (true) {

                        try {
                            Thread.sleep(RANDOM.nextLong(300, 10000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        registerRandomOperation(broker);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
     *
     * @param broker the broker that will observe the stocks.
     */
    private static void observeRandomStocks(Broker broker) {

        List<Asset> allAssets = new ArrayList<>(StockMarket.getInstance().getStocks());

        allAssets.stream().skip(RANDOM.nextInt(allAssets.size())).forEach(stock -> {
            System.out.println(ANSI_GREEN + "New stock observation! " + ANSI_RESET + broker + " is observing " + stock + ".");
            StockMarket.getInstance().getOperationBook(stock).observe(broker);
        });

    }

    /**
     * Registers a random operation. It can be an info operation or an offer operation.
     *
     * @param broker the broker that will register the operation.
     */
    private static void registerRandomOperation(SampleBroker broker) {

        Set<Asset> allAssets = StockMarket.getInstance().getStocks();
        Asset randomAsset = allAssets.stream().skip(RANDOM.nextInt(allAssets.size())).findFirst().orElse(null);
        Operation randomOperation;

        if (RANDOM.nextDouble() < 0.2D) {

            InfoOperation infoOperation = createRandomInfoOperation(broker, randomAsset);
            randomOperation = infoOperation;

            System.out.println(ANSI_GREEN + "New price request! " + ANSI_RESET + broker + " wants to know "
                    + infoOperation.getStock() + "'s price.");

        } else {

            OfferOperation offerOperation = createRandomOfferOperation(broker, randomAsset);
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
     * @param asset  the asset of the info operation to be created.
     * @return the created info operation.
     */
    private static InfoOperation createRandomInfoOperation(Broker broker, Asset asset) {

        Instant randomRecentInstant = Instant.now().minusSeconds(RANDOM.nextInt(10));

        return OperationFactory.createInfoOperation(
                broker, asset, randomRecentInstant,
                price -> {
                    String displayPrice = (price < 0 ? ANSI_RED + "[no executed offers yet]" : price) + ANSI_RESET;
                    System.out.println(broker + " requested the price of " + asset + " at " + randomRecentInstant + " and was answered with " + displayPrice + ".");
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
        double randomPrice = ((int) (RANDOM.nextDouble() * 10000)) / 100D; // random double value from 0 to 100 with two decimal cases

        return OperationFactory.createOfferOperation(
                broker, asset, randomOfferOperationType, randomQuantity, randomPrice
        );

    }

}

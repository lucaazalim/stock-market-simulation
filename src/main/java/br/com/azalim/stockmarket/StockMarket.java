package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.asset.Asset;
import br.com.azalim.stockmarket.asset.MarketType;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.broker.BrokerWallet;
import br.com.azalim.stockmarket.company.Company;
import br.com.azalim.stockmarket.observer.Observable;
import br.com.azalim.stockmarket.observer.impl.TransactionObserver;
import br.com.azalim.stockmarket.operation.OperationBook;
import br.com.azalim.stockmarket.operation.Operation;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents the stock market.
 */
public class StockMarket implements Observable<TransactionObserver> {

    /**
     * The singleton instance of this class.
     */
    private static StockMarket instance;

    /**
     * The operation books of the stocks.
     */
    private final Map<Asset, OperationBook> operationBooks = new HashMap<>();

    /**
     * The wallets of the brokers.
     */
    private final Map<Broker, BrokerWallet> wallets = new HashMap<>();

    /**
     * The observers of the transactions.
     */
    private final Set<TransactionObserver> observers = new HashSet<>();

    /**
     * The executor service that runs the processing of the registered operations.
     */
    private final ScheduledExecutorService operationProcessorExecutorService = Executors.newSingleThreadScheduledExecutor();

    public StockMarket(Set<Company> companies, Set<Broker> brokers) {

        if (instance != null) {
            throw new IllegalStateException("Asset market already instantiated");
        }

        companies
                .forEach(company -> company.getShareTypes().stream()
                        .mapMulti((shareType, consumer) -> {

                            Asset commonAsset = new Asset(company, shareType, MarketType.COMMON, null);
                            consumer.accept(commonAsset);

                            if (shareType.hasFractionalMarket()) {
                                consumer.accept(new Asset(company, shareType, MarketType.FRACTIONAL, commonAsset));
                            }

                        })
                        .map(Asset.class::cast)
                        .forEach(stock -> this.operationBooks.put(stock, new OperationBook())));

        brokers.forEach(broker -> this.wallets.put(broker, new BrokerWallet()));

        instance = this;

    }

    /**
     * Registers a new operation.
     * The operation will be registered in the operation book of the stock of the operation.
     *
     * @param operation the operation to be registered.
     */
    public void registerOperation(Operation operation) {
        this.getOperationBook(operation.getStock()).register(operation);
    }

    /**
     * Retrieves the operation book of a given asset.
     *
     * @param asset the asset of the operation book.
     * @return the operation book of the given asset or null if it does not exist.
     */
    public OperationBook getOperationBook(Asset asset) {
        return this.operationBooks.get(asset);
    }

    /**
     * @return an unmodifiable collection of all the operation books of the stock market.
     */
    public Collection<OperationBook> getOperationBooks() {
        return Collections.unmodifiableCollection(this.operationBooks.values());
    }

    /**
     * @return an unmodifiable set of all the stocks of the stock market.
     */
    public Set<Asset> getStocks() {
        return Collections.unmodifiableSet(this.operationBooks.keySet());
    }

    /**
     * Retrieves the wallet of a given broker.
     *
     * @param broker the owner of the wallet.
     * @return the wallet of the given broker or null if it does not exist.
     */
    public BrokerWallet getWallet(Broker broker) {
        return this.wallets.get(broker);
    }

    /**
     * Registers a new transaction.
     * The transaction will be registered in the wallets of the brokers.
     *
     * @param from     the broker that sold the shares.
     * @param to       the broker that bought the shares.
     * @param asset    the asset of the transaction.
     * @param quantity the quantity of shares transacted.
     * @param price    the price that was paid for the shares.
     */
    public void registerTransaction(Broker from, Broker to, Asset asset, int quantity, double price) {
        this.getWallet(from).registerTransaction(asset, new Transaction(-quantity, price));
        this.getWallet(to).registerTransaction(asset, new Transaction(quantity, price));
        this.observers.forEach(transactionObserver -> transactionObserver.onNewTransactionRegistered(from, to, asset, quantity, price));
    }

    /**
     * Starts processing the registered operations every one second.
     */
    public void startProcessingOperations() {

        this.getOperationBooks().forEach(operationBook -> this.operationProcessorExecutorService.scheduleWithFixedDelay(
                () -> {
                    try {
                        operationBook.getOperations().forEach(Operation::process);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                1, 1, TimeUnit.SECONDS
        ));

    }

    /**
     * Stops processing the registered operations.
     */
    public void stop() {
        this.operationProcessorExecutorService.shutdown();
    }

    /**
     * Subscribes to be notified of new transactions registered to the stock market.
     *
     * @param observer the observer that is going to be notified when new transactions are registered.
     */
    @Override
    public void observe(TransactionObserver observer) {
        this.observers.add(observer);
    }

    /**
     * @return the singleton instance of stock market.
     */
    public static StockMarket getInstance() {
        return instance;
    }

}

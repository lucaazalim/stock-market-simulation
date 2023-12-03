package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.observer.Observable;
import br.com.azalim.stockmarket.observer.impl.TransactionObserver;
import br.com.azalim.stockmarket.operation.OperationBook;
import br.com.azalim.stockmarket.operation.Operation;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Represents the stock market.
 */
public class StockMarket implements Observable<TransactionObserver> {

    /**
     * The singleton instance of this class.
     */
    private static final StockMarket instance = new StockMarket();

    /**
     * The operation books of the stocks.
     */
    private final Map<Stock, OperationBook> operationBooks = new HashMap<>();

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
     * Retrieves the operation book of a given stock.
     * If the operation book does not exist, it will be created.
     *
     * @param stock the stock of the operation book.
     * @return the operation book of the given stock.
     */
    public OperationBook getOperationBook(Stock stock) {
        return Optional.ofNullable(this.operationBooks.get(stock)).orElseGet(() -> {
            OperationBook operationBook = new OperationBook();
            this.operationBooks.put(stock, operationBook);
            return operationBook;
        });
    }

    /**
     * Retrieves the wallet of a given broker.
     * If the wallet does not exist, it will be created.
     *
     * @param broker the owner of the wallet.
     * @return the wallet of the given broker.
     */
    public BrokerWallet getWallet(Broker broker) {
        return Optional.ofNullable(this.wallets.get(broker)).orElseGet(() -> {
            BrokerWallet brokerWallet = new BrokerWallet();
            this.wallets.put(broker, brokerWallet);
            return brokerWallet;
        });
    }

    /**
     * Registers a new transaction.
     * The transaction will be registered in the wallets of the brokers.
     *
     * @param from     the broker that sold the shares.
     * @param to       the broker that bought the shares.
     * @param stock    the stock of the transaction.
     * @param quantity the quantity of shares transacted.
     * @param price    the price that was paid for the shares.
     */
    public void registerTransaction(Broker from, Broker to, Stock stock, int quantity, double price) {
        this.getWallet(from).registerTransaction(stock, new Transaction(-quantity, price));
        this.getWallet(to).registerTransaction(stock, new Transaction(quantity, price));
        this.observers.forEach(transactionObserver -> transactionObserver.onNewTransactionRegistered(from, to, stock, quantity, price));
    }

    /**
     * Starts processing the registered operations.
     */
    public void startProcessingOperations() {

        Stream.of(Stock.values()).forEach(stock -> this.operationProcessorExecutorService.scheduleWithFixedDelay(
                () -> this.getOperationBook(stock).getOperations().forEach(Operation::process),
                0, 1, TimeUnit.SECONDS
        ));

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

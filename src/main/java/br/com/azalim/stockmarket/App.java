package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.broker.B3Broker;
import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.company.B3Company;
import br.com.azalim.stockmarket.company.Company;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class App {

    public static void main(String[] args) {

        // These companies and brokers could be retrieved from a database or file.
        Set<Company> companies = new HashSet<>(Arrays.asList(B3Company.values()));
        Set<Broker> brokers = new HashSet<>(Arrays.asList(B3Broker.values()));

        // Initializes the stock market.
        StockMarket stockMarket = new StockMarket(companies, brokers);

        // Starts processing the registered operations.
        stockMarket.startProcessingOperations();

        // Starts the simulation.
        Simulation.start();

        // Registers a shutdown hook to stop the stock market.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stockMarket.stopProcessingOperations();
            Simulation.stop();
        }));

    }

}

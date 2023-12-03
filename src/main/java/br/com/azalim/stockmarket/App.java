package br.com.azalim.stockmarket;

import br.com.azalim.stockmarket.broker.Broker;
import br.com.azalim.stockmarket.broker.SampleBroker;
import br.com.azalim.stockmarket.company.Company;
import br.com.azalim.stockmarket.company.SampleCompany;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {

        // These companies and brokers could be retrieved from a database or file.
        Set<Company> companies = new HashSet<>(Arrays.asList(SampleCompany.values()));
        Set<Broker> brokers = new HashSet<>(Arrays.asList(SampleBroker.values()));

        // Initializes the stock market.
        StockMarket stockMarket = new StockMarket(companies, brokers);

        System.out.println(stockMarket.getStocks());

        // Starts processing the registered operations.
        stockMarket.startProcessingOperations();

        // Starts the simulation.
        Simulation.start();

    }

}

package br.com.azalim.stockmarket;

public class App {

    public static void main(String[] args) {

        StockMarket.getInstance().startProcessingOperations();
        Simulation.start();

    }

}

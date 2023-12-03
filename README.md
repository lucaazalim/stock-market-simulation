# Stock Market Simulation

This is a Java application that simulates a stock market, slightly inspired by the brazilian stock market ([B3](https://www.b3.com.br/en_us/)). It was built as a assignment for the Modular Programming course at the PUC Minas university during my Software Engineering program.

To build and run the simulation, make sure you have the following installed:

- Java SDK 20 or higher
- Maven 4.0.0 or higher

The main class is located at `src/main/java/br/com/azalim/stockmarket/App.java`. When executed, it initiates the simulation, creating a thread for each broker. These threads observe random stocks and register random buy, sell and info operations to the stock market.

The code is thoroughly documented and should be **easy to understand**.
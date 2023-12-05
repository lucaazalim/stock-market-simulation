# Stock Market Simulation

This is a Java application that simulates a stock market, slightly inspired by the brazilian stock market ([B3](https://www.b3.com.br/en_us/)). It was developed as a final assignment for the Modular Programming course as part of my Software Engineering program at PUC Minas University.

As the project purpose is to learn about modular programming, it explores a bunch of advanced concepts, such as:

- Design Patterns (Decorator, Observer, Singleton and Factory)
- SOLID principles
- Unit testing and mocking

The code is thoroughly documented and should be **easy to understand and add new features**.

---

To build and run the simulation, make sure you have the following installed:

- Java SDK 20 or higher
- Maven 4.0.0 or higher

The main class is called `App.java`. When executed, it initiates the simulation, creating a thread for each broker. These threads observe random stocks and register random buy, sell and info operations to the stock market. There is also a thread for processing the registered operations to each operation book.
package br.com.azalim.stockmarket;

import static br.com.azalim.stockmarket.PrintColor.*;

/**
 * Represents a stock.
 */
public enum Stock {

    PETR4("Petróleo Brasileiro S.A. - Petrobras", "Exploração, produção, refino e comercialização de petróleo e gás natural."),
    VALE3("Vale S.A.", "Mineração, logística, energia e siderurgia."),
    ITUB4("Itaú Unibanco Holding S.A.", "Serviços bancários, como operações de crédito e financiamentos."),
    BBDC4("Banco Bradesco S.A.", "Serviços bancários, seguros, previdência e capitalização."),
    B3SA3("B3 S.A. - Brasil, Bolsa, Balcão", "Infraestrutura para negociação de ativos financeiros."),
    JBSS3("JBS S.A.", "Produção e comercialização de carne bovina, suína, frango e produtos relacionados."),
    LREN3("Lojas Renner S.A.", "Varejo de moda."),
    VVAR3("Via Varejo S.A.", "Comércio varejista de eletrodomésticos, móveis e produtos eletrônicos."),
    GGBR4("Gerdau S.A.", "Produção de aço."),
    CIEL3("Cielo S.A.", "Meios de pagamento eletrônicos.");

    private final String name, description;

    /**
     * Creates a new stock with the given name and description.
     *
     * @param name the name of the stock.
     * @param description the description of the stock.
     */
    Stock(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return the unique ticker of the stock.
     */
    public String getTicker() {
        return this.name();
    }

    /**
     * @return the name of the company.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the description of the company.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return a colored string containing the ticker of the stock.
     */
    @Override
    public String toString() {
        return ANSI_YELLOW + this.name() + ANSI_RESET;
    }
}

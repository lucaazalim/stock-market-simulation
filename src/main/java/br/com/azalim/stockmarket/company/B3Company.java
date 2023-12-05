package br.com.azalim.stockmarket.company;

import br.com.azalim.stockmarket.asset.AssetType;

import java.util.Set;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Represents some of the companies that are available in the B3 stock market.
 */
public enum B3Company implements Company {

    PETR(
            "Petróleo Brasileiro S.A. - Petrobras",
            "Exploração, produção, refino e comercialização de petróleo e gás natural.",
            AssetType.COMMON, AssetType.PREFERRED
    ),
    VALE(
            "Vale S.A.",
            "Mineração, logística, energia e siderurgia.",
            AssetType.COMMON
    ),
    ITUB(
            "Itaú Unibanco Holding S.A.",
            "Serviços bancários, como operações de crédito e financiamentos.",
            AssetType.COMMON, AssetType.PREFERRED
    ),
    BBDC(
            "Banco Bradesco S.A.",
            "Serviços bancários, seguros, previdência e capitalização.",
            AssetType.COMMON, AssetType.PREFERRED
    ),
    B3SA(
            "B3 S.A. - Brasil, Bolsa, Balcão",
            "Infraestrutura para negociação de ativos financeiros.",
            AssetType.COMMON, AssetType.PREFERRED
    ),
    JBSS(
            "JBS S.A.",
            "Produção e comercialização de carne bovina, suína, frango e produtos relacionados.",
            AssetType.COMMON
    ),
    LREN(
            "Lojas Renner S.A.",
            "Varejo de moda.",
            AssetType.COMMON
    ),
    VVAR(
            "Via Varejo S.A.",
            "Comércio varejista de eletrodomésticos, móveis e produtos eletrônicos.",
            AssetType.COMMON
    ),
    GGBR(
            "Gerdau S.A.",
            "Produção de aço.",
            AssetType.COMMON, AssetType.PREFERRED
    ),
    CIEL(
            "Cielo S.A.",
            "Meios de pagamento eletrônicos.",
            AssetType.COMMON
    ),
    XPLG(
            "XP Log FII",
            "Gestão ativa focada em investimentos imobiliários no segmento logístico.",
            AssetType.UNITS
    );

    private final String name, description;
    private final Set<AssetType> assetTypes;

    /**
     * Creates a new stock with the given name and description.
     *
     * @param name        the name of the stock.
     * @param description the description of the stock.
     */
    B3Company(String name, String description, AssetType... assetTypes) {
        this.name = name;
        this.description = description;
        this.assetTypes = Set.of(assetTypes);
    }

    @Override
    public String getSymbol() {
        return this.name();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Set<AssetType> getAssetTypes() {
        return this.assetTypes;
    }

    /**
     * @return a colored string containing the ticker of the stock.
     */
    @Override
    public String toString() {
        return ansi().fgYellow() + this.name() + ansi().reset();
    }

}

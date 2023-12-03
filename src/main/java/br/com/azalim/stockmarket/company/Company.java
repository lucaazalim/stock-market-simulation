package br.com.azalim.stockmarket.company;

import br.com.azalim.stockmarket.asset.AssetType;

import java.util.Set;

public interface Company {

    String getSymbol();

    String getName();

    String getDescription();

    Set<AssetType> getShareTypes();

}

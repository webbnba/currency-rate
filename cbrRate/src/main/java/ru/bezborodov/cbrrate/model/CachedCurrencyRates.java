package ru.bezborodov.cbrrate.model;

import lombok.Value;

import java.util.List;

@Value
public class CachedCurrencyRates {
    List<CurrencyRate> currencyRates;
}

package ru.bezborodov.cbrrate.parser;

import ru.bezborodov.cbrrate.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateParser {

    List<CurrencyRate> parse(String ratesAsString);
}

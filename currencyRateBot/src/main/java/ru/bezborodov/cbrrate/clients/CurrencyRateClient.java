package ru.bezborodov.cbrrate.clients;

import ru.bezborodov.cbrrate.model.CurrencyRate;

import java.time.LocalDate;

public interface CurrencyRateClient {
    CurrencyRate getCurrencyRate(String rateType, String currency, LocalDate date);
}

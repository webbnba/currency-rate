package ru.cbrrate.service;

public class CurrencyRateNotFoundException extends RuntimeException {
    public CurrencyRateNotFoundException(String message) {
        super(message);
    }
}

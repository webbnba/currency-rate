package ru.bezborodov.cbrrate.clients;

public class CurrencyRateClientException extends RuntimeException {
    public CurrencyRateClientException(String msg) {
        super(msg);
    }
}

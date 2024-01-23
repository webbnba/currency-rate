package ru.cbrrate.clients;

import ru.cbrrate.model.CurrencyRate;

public interface HttpClient {
    String performRequest(String url);
}

package ru.bezborodov.cbrrate.clients;

public interface HttpClient {
    String performRequest(String url, String params);
}

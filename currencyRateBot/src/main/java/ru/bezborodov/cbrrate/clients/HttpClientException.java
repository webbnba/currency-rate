package ru.bezborodov.cbrrate.clients;

public class HttpClientException extends RuntimeException {
    public HttpClientException(String msg) {
        super(msg);
    }
}

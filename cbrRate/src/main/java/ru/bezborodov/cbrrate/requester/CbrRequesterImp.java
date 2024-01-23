package ru.bezborodov.cbrrate.requester;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class CbrRequesterImp implements CbrRequester {

    @Override
    public String getRatesAsXml(String url) {
        try {
            log.info("request for url:{}", url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception ex) {
            if(ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("cbr request error, url:{}", url);
            throw new RequesterException(ex);
        }
    }
}

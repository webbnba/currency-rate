package ru.bezborodov.cbrrate.clients;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bezborodov.cbrrate.config.CurrencyRateClientConfig;
import ru.bezborodov.cbrrate.model.CurrencyRate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateClientImpl implements CurrencyRateClient {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CurrencyRateClientConfig config;
    private final ObjectMapper objectMapper;

    @Override
    public CurrencyRate getCurrencyRate(String rateType, String currency, LocalDate date) {
        log.info("getCurrencyRate, rateType:{}, currency:{}, date:{}", rateType, currency, date);
        var urlWithParams = String.format("%s/%s/%s/%s", config.getUrl(), rateType, currency, DATE_FORMATTER.format(date));
        var client = java.net.http.HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), CurrencyRate.class);
        } catch (JsonParseException ex) {
            log.error("Exception");
            throw new CurrencyRateClientException("Error from Cbr client host" + ex.getMessage());
        } catch (Exception ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Http request error, url:{}", urlWithParams, ex);
            throw new HttpClientException(ex.getMessage());
        }
    }
}

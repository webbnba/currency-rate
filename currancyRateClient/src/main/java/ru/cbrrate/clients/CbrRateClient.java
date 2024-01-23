package ru.cbrrate.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cbrrate.config.CbrRateClientConfig;
import ru.cbrrate.model.CurrencyRate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service("cbr")
@RequiredArgsConstructor
@Slf4j
public class CbrRateClient implements RateClient {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CbrRateClientConfig config;
    private final ru.cbrrate.clients.HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Override
    public CurrencyRate getCurrencyRate(String currency, LocalDate date) {
        log.info("getCurrencyRate currency:{}, date:{}", currency, date);
        String urlWithParams = String.format("%s/%s/%s", config.getUrl(), currency, DATE_FORMATTER.format(date));

        try{
            String response = httpClient.performRequest(urlWithParams);
            return objectMapper.readValue(response, CurrencyRate.class);
        } catch (HttpClientException ex) {
            throw new RateClientException("Error from Cbr Client host:" + ex.getMessage());
        } catch (Exception e) {
            log.error("Getting currency rate error, currency:{}, date:{}", currency, date);
            throw new RateClientException("Can't get currencyRate, currency: " + currency + ", date: " + date);
        }
    }
}

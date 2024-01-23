package ru.bezborodov.cbrrate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import ru.bezborodov.cbrrate.config.CbrConfig;
import ru.bezborodov.cbrrate.model.CachedCurrencyRates;
import ru.bezborodov.cbrrate.model.CurrencyRate;
import ru.bezborodov.cbrrate.parser.CurrencyRateParser;
import ru.bezborodov.cbrrate.requester.CbrRequester;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateService {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CbrRequester cbrRequester;
    private final CurrencyRateParser currencyRateParser;
    private final CbrConfig cbrConfig;
    private final Cache<LocalDate, CachedCurrencyRates> currencyRateCache;

    public CurrencyRate getCurrencyRate(String currency, LocalDate date) {
        log.info("getCurrencyRate. currency:{}, date:{}", currency, date);
        List<CurrencyRate> rates;

        CachedCurrencyRates cachedCurrencyRates = currencyRateCache.get(date);
        if(cachedCurrencyRates == null) {
            String urlWithParams = String.format("%s?date_req=%s", cbrConfig.getUrl(), DATE_FORMATTER.format(date));
            String ratesAsXml = cbrRequester.getRatesAsXml(urlWithParams);
            rates = currencyRateParser.parse(ratesAsXml);
            currencyRateCache.put(date, new CachedCurrencyRates(rates));
        } else {
            rates = cachedCurrencyRates.getCurrencyRates();
        }

        return rates.stream().filter(rate -> currency.equals(rate.getCharCode()))
                .findFirst()
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency rate not found. Currency:" +
                                                                     currency + ", date:" + date));
    }

}

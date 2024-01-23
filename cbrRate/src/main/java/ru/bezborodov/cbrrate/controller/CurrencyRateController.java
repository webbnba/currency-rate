package ru.bezborodov.cbrrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bezborodov.cbrrate.model.CurrencyRate;
import ru.bezborodov.cbrrate.service.CurrencyRateService;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "${app.rest.api.prefix}/v1")
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping("/currencyRate/{currency}/{date}")
    public CurrencyRate getCurrencyRate(@PathVariable("currency") String currency,
                                        @PathVariable("date")
                                        @DateTimeFormat(pattern = "dd-MM-yyyy")
                                        LocalDate date) {
        log.info("getCurrencyRate, currency:{}, date:{}", currency, date);
        CurrencyRate rate = currencyRateService.getCurrencyRate(currency, date);
        log.info("currencyRate:{}", rate);
        return rate;
    }
}

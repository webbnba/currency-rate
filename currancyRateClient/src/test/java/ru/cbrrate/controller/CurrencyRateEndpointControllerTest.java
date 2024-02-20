package ru.cbrrate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import ru.cbrrate.config.ApplicationConfig;
import ru.cbrrate.config.CbrRateClientConfig;
import ru.cbrrate.config.JsonConfig;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(CurrencyRateEndpointController.class)
@Import({ApplicationConfig.class, JsonConfig.class, CbrRateClientConfig.class})
class CurrencyRateEndpointControllerTest {

    @Test
    void getCurrencyRate() {
    }
}
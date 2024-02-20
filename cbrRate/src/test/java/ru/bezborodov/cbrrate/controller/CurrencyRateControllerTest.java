package ru.bezborodov.cbrrate.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.bezborodov.cbrrate.config.ApplicationConfig;
import ru.bezborodov.cbrrate.config.CbrConfig;
import ru.bezborodov.cbrrate.config.JsonConfig;
import ru.bezborodov.cbrrate.parser.CurrencyRateParserXml;
import ru.bezborodov.cbrrate.requester.CbrRequester;
import ru.bezborodov.cbrrate.service.CurrencyRateService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@WebMvcTest(CurrencyRateController.class)
@Import({ApplicationConfig.class, JsonConfig.class, CurrencyRateService.class, CurrencyRateParserXml.class})
class CurrencyRateControllerTest {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CbrConfig cbrConfig;

    @MockBean
    CbrRequester cbrRequester;

    @Test
    @DirtiesContext
    void getCurrencyRateTest() throws Exception {
        //given
        var currency = "EUR";
        var date = "20-01-2024";
        prepareCbrRequesterMock(date);

        var result = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/currencyRate/%s/%s", currency, date)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertThat(result).isEqualTo("{\"numCode\":\"978\",\"charCode\":\"EUR\",\"nominal\":\"1\",\"name\":\"Евро\",\"value\":\"96,3835\"}");
    }

    @Test
    @DirtiesContext
    void cacheUseTest() throws Exception {
        //given
        prepareCbrRequesterMock(null);

        var currency = "EUR";
        var date = "20-01-2024";

        //when
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());

        currency = "USD";
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());
        date = "21-01-2024";
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());
        //then
        Mockito.verify(cbrRequester, Mockito.times(2)).getRatesAsXml(Mockito.any());
    }
    private void prepareCbrRequesterMock(String date) throws URISyntaxException, IOException {
        URI uri = ClassLoader.getSystemResource("XML_daily.xml").toURI();
        String ratesXml = Files.readString(Paths.get(uri), Charset.forName("Windows-1251"));

        if(date == null) {
            Mockito.when(cbrRequester.getRatesAsXml(Mockito.any())).thenReturn(ratesXml);
        } else {
            LocalDate dateParam = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String cbrUrl = String.format("%s?date_req=%s", cbrConfig.getUrl(), DATE_FORMATTER.format(dateParam));
            Mockito.when(cbrRequester.getRatesAsXml(cbrUrl)).thenReturn(ratesXml);
        }

    }
}
package ru.bezborodov.cbrrate.parser;

import org.junit.jupiter.api.Test;
import ru.bezborodov.cbrrate.model.CurrencyRate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CurrencyRateParserXmlTest {

    @Test
    void parseTest() throws URISyntaxException, IOException {
        //given
        var parser = new CurrencyRateParserXml();
        URI uri = ClassLoader.getSystemResource("XML_daily.xml").toURI();
        String ratesXml = Files.readString(Paths.get(uri), Charset.forName("Windows-1251"));

        //when
        List<CurrencyRate> rates = parser.parse(ratesXml);

        //then
        assertThat(rates.size()).isEqualTo(43);
        assertThat(rates.contains(getUSDrate())).isTrue();
        assertThat(rates.contains(getEURrate())).isTrue();
        assertThat(rates.contains(getJPYrate())).isTrue();

    }

    private CurrencyRate getUSDrate() {
        return CurrencyRate.builder()
                .numCode("840")
                .charCode("USD")
                .nominal("1")
                .name("Доллар США")
                .value("88,5896")
                .build();
    }

    private CurrencyRate getEURrate() {
        return CurrencyRate.builder()
                .numCode("978")
                .charCode("EUR")
                .nominal("1")
                .name("Евро")
                .value("96,3835")
                .build();
    }

    private CurrencyRate getJPYrate() {
        return CurrencyRate.builder()
                .numCode("392")
                .charCode("JPY")
                .nominal("100")
                .name("Японских иен")
                .value("59,8255")
                .build();
    }
}

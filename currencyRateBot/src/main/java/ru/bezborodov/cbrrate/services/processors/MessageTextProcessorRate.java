package ru.bezborodov.cbrrate.services.processors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bezborodov.cbrrate.clients.CurrencyRateClient;
import ru.bezborodov.cbrrate.model.CurrencyRate;
import ru.bezborodov.cbrrate.model.MessageTextProcessorResult;
import ru.bezborodov.cbrrate.services.DateTimeProvider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@AllArgsConstructor
@Service("messageTextProcessorRate")
public class MessageTextProcessorRate implements MessageTextProcessor {

    private static final String CBR_RATE_CONST = "CBR";
    private static final String DATE_FORMAT_ZERO = "dd-MM-yyyy";
    private static final String DATE_FORMAT = "d-MM-yyyy";
    private static final DateTimeFormatter DATE_FORMATTER_ZERO = DateTimeFormatter.ofPattern(DATE_FORMAT_ZERO);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CurrencyRateClient currencyRateClient;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public MessageTextProcessorResult process(String msgText) {
        log.info("msgText:{}", msgText);

        String[] textParts = msgText.split(" ");

        if (textParts.length < 1 || textParts.length > 3) {
            return new MessageTextProcessorResult(null, Message.EXPECTED_FORMAT_MESSAGE.getText());
        }

        String rateType = null;
        String currency = null;
        String dateAsString = null;
        LocalDate date = null;

        if (textParts.length == 3) {
            rateType = textParts[0];
            currency = textParts[1];
            dateAsString = textParts[2];
        }

        if (textParts.length == 2) {
            rateType = CBR_RATE_CONST;
            currency = textParts[0];
            dateAsString = textParts[1];
        }

        if (textParts.length == 1) {
            rateType = CBR_RATE_CONST;
            currency = textParts[0];
            date = dateTimeProvider.get().toLocalDate();
        }

        if (textParts.length == 1 && msgText.startsWith("/")) {
            rateType = CBR_RATE_CONST;
            currency = msgText.substring(1);
            date = dateTimeProvider.get().toLocalDate();
        }

        if (textParts.length == 3 || textParts.length == 2) {
            try {
                date = parseDate(dateAsString);
            } catch (Exception ex) {
                log.error("parsing error, string:{}", dateAsString, ex);
                return new MessageTextProcessorResult(null, Message.DATA_FORMAT_MESSAGE.getText());
            }
        }

        if (rateType == null || currency == null) {
            log.error("rateType:{} of currency:{} is null", rateType, currency);
            throw new IllegalArgumentException("rateType:" + rateType + " or currency" + currency + " is null");
        }

        CurrencyRate rate = currencyRateClient.getCurrencyRate(rateType.toUpperCase(), currency.toUpperCase(), date);
        return new MessageTextProcessorResult(rate.getValue(), null);
    }

        private LocalDate parseDate(String dateAsString) {
            try {
                return LocalDate.parse(dateAsString, DATE_FORMATTER_ZERO);
            } catch (Exception ex) {
                return LocalDate.parse(dateAsString, DATE_FORMATTER);
            }
        }
}

package ru.bezborodov.cbrrate.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bezborodov.cbrrate.config.TelegramClientConfig;
import ru.bezborodov.cbrrate.model.GetUpdatesRequest;
import ru.bezborodov.cbrrate.model.GetUpdatesResponse;
import ru.bezborodov.cbrrate.model.SendMessageRequest;
import ru.bezborodov.cbrrate.services.TelegramException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramClientImpl implements TelegramClient{
    private final HttpClient httpClientJdk;
    private final ObjectMapper objectMapper;
    private final TelegramClientConfig clientConfig;

    @Override
    public GetUpdatesResponse getUpdates(GetUpdatesRequest request) {
        try {
            var params = objectMapper.writeValueAsString(request);
            log.info("getUpdates params:{}", params);

            var updatesAsString = httpClientJdk.performRequest(makeUrl("getUpdates"), params);
            log.info("updatesAsString:{}", updatesAsString);
            var updates = objectMapper.readValue(updatesAsString, GetUpdatesResponse.class);
            log.info("updates:{}", updates);

            return updates;
        } catch (JsonProcessingException ex) {
            log.error("request:{}", request, ex);
            throw new TelegramException(ex);
        }
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        try {
            var params = objectMapper.writeValueAsString(request);
            log.info("params:{}", params);

            var responseAsString = httpClientJdk.performRequest(makeUrl("sendMessage"), params);
            log.info("responseAsString:{}", responseAsString);
        } catch (JsonProcessingException ex) {
            log.error("request:{}", request, ex);
            throw new TelegramException(ex);
        }
    }

    private String makeUrl(String method) {
        return String.format("%s/bot%s/%s", clientConfig.getUrl(), clientConfig.getToken(), method);
    }
}

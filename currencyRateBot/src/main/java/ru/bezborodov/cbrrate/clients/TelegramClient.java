package ru.bezborodov.cbrrate.clients;

import ru.bezborodov.cbrrate.model.GetUpdatesRequest;
import ru.bezborodov.cbrrate.model.GetUpdatesResponse;
import ru.bezborodov.cbrrate.model.SendMessageRequest;

public interface TelegramClient {

    GetUpdatesResponse getUpdates(GetUpdatesRequest request);

    void sendMessage(SendMessageRequest request);
}

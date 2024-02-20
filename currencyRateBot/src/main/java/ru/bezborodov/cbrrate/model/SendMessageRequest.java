package ru.bezborodov.cbrrate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SendMessageRequest {

    @JsonProperty("chat_id")
    long chatId;

    @JsonProperty("text")
    String text;

    @JsonProperty("replay_to_message_id")
    long replayMessageId;
}

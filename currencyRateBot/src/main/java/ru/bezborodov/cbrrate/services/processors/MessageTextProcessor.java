package ru.bezborodov.cbrrate.services.processors;

import ru.bezborodov.cbrrate.model.MessageTextProcessorResult;

public interface MessageTextProcessor {
    MessageTextProcessorResult process(String msgText);
}

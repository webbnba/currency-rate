package ru.bezborodov.cbrrate.services;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeProviderCurrent implements DateTimeProvider{
    @Override
    public LocalDateTime get() {
        return LocalDateTime.now();
    }
}

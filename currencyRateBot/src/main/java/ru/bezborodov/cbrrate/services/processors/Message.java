package ru.bezborodov.cbrrate.services.processors;

public enum Message {
    DATA_FORMAT_MESSAGE("Ожидаемый формат даты:\n" + "dd-MM-yyyy, Пример: 22-01-2024"),
    EXPECTED_FORMAT_MESSAGE("""
            Возможные команды ("/" не нужен)
            Курс на дату: CBR USD dd-MM-yyyy
            Курс ЦБ на дату: EUR dd-MM-yyyy
            Курс ЦБ на сегодня: USD
            """);

    private final String text;

    Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

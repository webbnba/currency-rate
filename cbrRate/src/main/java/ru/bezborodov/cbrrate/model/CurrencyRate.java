package ru.bezborodov.cbrrate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Value
@Builder
public class CurrencyRate {
    String numCode;
    String charCode;
    String nominal;
    String name;
    String value;
}

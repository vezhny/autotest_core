package com.vezh.lab.ui.pages.example;

import com.vezh.lab.core.exception.UiEngineExecutionException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("male"),
    FEMALE("female"),
    OTHER("my_business");

    private String value;

    /**
     * Gets a gender according to given value (from Selenide element)
     * @param value - target value
     * @return
     */
    @SneakyThrows
    public static Gender getGender(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.value.equals(value)) {
                return gender;
            }
        }
        throw new UiEngineExecutionException("Unable to resolve gender for value \"" + value + "\"");
    }
}

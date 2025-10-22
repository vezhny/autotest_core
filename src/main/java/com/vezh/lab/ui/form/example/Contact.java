package com.vezh.lab.ui.form.example;

import com.vezh.lab.core.exception.UiEngineExecutionException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public enum Contact {

    EMAIL("checkbox1"),
    SMS("checkbox2");

    private String id;

    /**
     * Gets a contact according to given id
     * @param id - target id
     * @return
     */
    @SneakyThrows
    public static Contact getContact(String id) {
        for (Contact contact : values()) {
            if (contact.id.equals(id)) {
                return contact;
            }
        }
        throw new UiEngineExecutionException("Unable for resolve contact for id: \"" + id + "\"");
    }
}

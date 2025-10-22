package com.vezh.lab.ui.form.example;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
public class ExampleForm {

    private String firstName;
    private String surname;
    private Gender gender;
    private Color color;
    private List<Contact> contacts;
    private String moreInfo;
    private Continent continent;

    /**
     * Adds given contacts into list
     * @param contacts - contacts to add
     * @return
     */
    public ExampleForm addContact(Contact... contacts) {
        this.contacts = Optional.ofNullable(this.contacts).orElse(new ArrayList());
        this.contacts.addAll(Arrays.asList(contacts));
        return this;
    }
}

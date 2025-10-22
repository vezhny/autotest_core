package com.vezh.lab.ui.form.example;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
}

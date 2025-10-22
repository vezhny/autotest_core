package com.vezh.lab.api.model.example;

import com.vezh.lab.api.model.ModelCore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExampleModel extends ModelCore {

    private Long id;
    private String name;
    private String company;
    private String username;
    private String email;
    private String address;
    private String zip;
    private String state;
    private String country;
    private String phone;
    private String photo;
}
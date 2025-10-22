package com.vezh.lab.core.matcher;

import com.vezh.lab.api.model.example.ExampleModel;
import com.vezh.lab.ui.form.example.*;
import com.vezh.lab.ui.pages.example.ExamplePage;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExampleMatcher extends MatcherCore {

    /**
     * Asserts example response body
     * @param response - actual RestAssured response
     */
    @Step("Assert example models")
    public void assertExampleModels(Response response) {
        ExampleModel[] models = response.as(ExampleModel[].class);
        assertTrue(models.length > 0, "models arrays is nt empty");
    }

    /**
     * Asserts, that page is filled according to the form
     * @param form - expected form
     * @param page - actual UI page
     */
    @Step("Assert, that page is filled")
    public void assertExamplePageIsFilled(ExampleForm form, ExamplePage page) {
        assertEquals(form.getFirstName(), page.getFirstNameValue(), "first name");
        assertEquals(form.getSurname(), page.getSurNameValue(), "surname");

        Gender selectedGender = page.getSelectedGender();
        assertValueExists(selectedGender, "gender");
        assertEquals(form.getGender(), selectedGender, "gender");

        Color selectedColor = page.getSelectedColor();
        assertValueExists(selectedColor, "color");
        assertEquals(form.getColor(), selectedColor, "color");

        List<Contact> selectedContacts = page.getSelectedContacts();
        assertEquals(form.getContacts().size(), selectedContacts.size(), "contacts size");
        assertListHas(selectedContacts, "contacts", form.getContacts().toArray(new Contact[]{}));

        assertEquals(form.getMoreInfo(), page.getMoreInfoText(), "more info");

        Continent selectedContinent = page.getSelectedContinent();
        assertValueExists(selectedContinent, "continent");
        assertEquals(form.getContinent(), selectedContinent, "continent");
    }

    /**
     * Asserts that page is blank
     * @param page - actual UI page
     */
    @Step("Assert, that page is blank")
    public void assertPageIsBlank(ExamplePage page) {
        assertTrue(page.getFirstNameValue().isEmpty(), "first name is blank");
        assertTrue(page.getSurNameValue().isEmpty(), "surname is blank");
        assertValueAbsent(page.getSelectedGender(), "selected gender");
        assertValueAbsent(page.getSelectedColor(), "selected color");
        assertTrue(page.getSelectedContacts().isEmpty(), "no selected contacts");
        assertTrue(page.getMoreInfoText().isEmpty(), "more info is blank");
        assertValueAbsent(page.getSelectedContinent(), "selected continent");
    }
}

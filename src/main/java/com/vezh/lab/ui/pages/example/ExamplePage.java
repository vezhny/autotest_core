package com.vezh.lab.ui.pages.example;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vezh.lab.core.exception.UiEngineExecutionException;
import com.vezh.lab.ui.form.example.*;
import com.vezh.lab.ui.pages.PageCore;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

@Component
public class ExamplePage extends PageCore {

    private final SelenideElement firstNameInput = $(By.id("firstname"));
    private final SelenideElement surNameInput = $(By.id("surname"));
    private final SelenideElement genderSelect = $(By.id("gender"));
    private final ElementsCollection genderOptions = genderSelect.findAll(By.tagName("option"));
    private final ElementsCollection colors = $$x(".//input[@type='radio']");
    private final SelenideElement textArea = $(By.tagName("textarea"));
    private final SelenideElement continentSelect = $(By.id("continent"));
    private final ElementsCollection continentOptions = continentSelect.findAll(By.tagName("option"));
    private final ElementsCollection contacts = $$x(".//input[@type='checkbox']");
    private final SelenideElement submitButton = $(By.id("submitbutton"));

    @Override
    public ExamplePage load() {
        checkVisible(firstNameInput);
        checkVisible(surNameInput);
        checkVisible(genderSelect);
        checkCondition(colors, CollectionCondition.size(2));
        checkVisible(textArea);
        checkVisible(continentSelect);
        checkCondition(continentOptions, CollectionCondition.sizeGreaterThan(2));
        checkCondition(contacts, CollectionCondition.size(2));
        checkVisible(submitButton);
        return this;
    }

    @Override
    public Boolean isCurrent() {
        return firstNameInput.isDisplayed() && submitButton.isDisplayed();
    }

    /**
     * Fills the page according to given form
     * ! All the form should be filled completely. There are no NPE countermeasures !
     * @param form - given form
     * @return
     */
    @Step("Fill the page")
    public ExamplePage fillThePage(ExampleForm form) {
        enterFirstName(form.getFirstName());
        enterSurName(form.getSurname());
        selectGender(form.getGender());
        selectColor(form.getColor());
        selectContacts(form.getContacts().toArray(new Contact[]{}));
        enterMoreText(form.getMoreInfo());
        selectContinent(form.getContinent());
        return this;
    }

    /**
     * Enters given value into first name input
     * @param firstName - value to enter
     * @return
     */
    @Step("Enter first name \"{0}\"")
    public ExamplePage enterFirstName(String firstName) {
        firstNameInput.append(firstName);
        return this;
    }

    /**
     * Clears first name input
     * @return
     */
    @Step("Clear first name")
    public ExamplePage clearFirstName() {
        firstNameInput.clear();
        return this;
    }

    /**
     * Enters given value into surname input
     * @param surName - value to enter
     * @return
     */
    @Step("Enter surname \"{0}\"")
    public ExamplePage enterSurName(String surName) {
        surNameInput.append(surName);
        return this;
    }

    /**
     * Clears first surinput
     * @return
     */
    @Step("Clear surname")
    public ExamplePage clearSurName() {
        surNameInput.clear();
        return this;
    }

    /**
     * Selects gender
     * @param gender - target gender to select
     * @return
     */
    @Step("Select gender {0}")
    public ExamplePage selectGender(Gender gender) {
        SelenideElement option = getGenderOption(gender);
        option.click();
        return this;
    }

    /**
     * Selects color radio button
     * @param color - target color
     * @return
     */
    @Step("Select color {0}")
    public ExamplePage selectColor(Color color) {
        SelenideElement radioButton = getColorElement(color);
        radioButton.click();
        return this;
    }

    /**
     * Selects all given contacts
     * @param contacts - contacts to select
     * @return
     */
    @Step("Select contacts")
    public ExamplePage selectContacts(Contact... contacts) {
        for (Contact contact : contacts) {
            selectContact(contact);
        }
        return this;
    }

    /**
     * Selects contact checkbox
     * @param contact - target contact
     * @return
     */
    @Step("Select contact {0}")
    public ExamplePage selectContact(Contact contact) {
        SelenideElement checkbox = getContactCheckbox(contact);
        setCheckboxCheckedTo(checkbox, true);
        return this;
    }

    /**
     * Selects continent option
     * @param continent - target continent
     * @return
     */
    @Step("Select continent {0}")
    public ExamplePage selectContinent(Continent continent) {
        SelenideElement option = getContinentOption(continent);
        option.click();
        return this;
    }

    /**
     * Enters given value into textarea
     * @param text - value to enter
     * @return
     */
    @Step("Enter more info")
    public ExamplePage enterMoreText(String text) {
        textArea.append(text);
        return this;
    }

    /**
     * Clears textarea
     * @return
     */
    @Step("Clear more info")
    public ExamplePage clearMoreText() {
        textArea.clear();
        return this;
    }

    /**
     * Clicks submit button
     * @return
     */
    @Step("Submit button click")
    public ExamplePage clickSubmitButton() {
        submitButton.click();
        return this;
    }

    /**
     * Gets a first name input value
     * @return
     */
    public String getFirstNameValue() {
        return firstNameInput.getValue();
    }

    /**
     * Gets a surname input value
     * @return
     */
    public String getSurNameValue() {
        return surNameInput.getValue();
    }

    /**
     * Gets a selected gender
     * Returns null, if not selected. Be aware of NPE
     * @return
     */
    public Gender getSelectedGender() {
        if (genderSelect.getValue().isEmpty()) {
            return null;
        }
        return Gender.getGender(genderSelect.getValue());
    }

    /**
     * Gets a selected color
     * Returns null, if not selected. Be aware of NPE
     * @return
     */
    public Color getSelectedColor() {
        SelenideElement selectedRadio = null;
        for (SelenideElement color : colors) {
            if (color.has(Condition.checked)) {
                selectedRadio = color;
            }
        }
        if (selectedRadio == null) {
            return null;
        }
        return Color.valueOf(selectedRadio.getAttribute("id").toUpperCase());
    }

    /**
     * Gets selected contacts as ArrayList
     * @return
     */
    public List<Contact> getSelectedContacts() {
        List<Contact> selectedContacts = new ArrayList<>();
        for (SelenideElement contact : contacts) {
            if (contact.has(Condition.checked)) {
                selectedContacts.add(Contact.getContact(contact.getAttribute("id")));
            }
        }
        return selectedContacts;
    }

    /**
     * Gets text from textarea
     * @return
     */
    public String getMoreInfoText() {
        return textArea.getValue();
    }

    /**
     * Gets a selected continent
     * Returns null, if not selected. Be aware of NPE
     * @return
     */
    public Continent getSelectedContinent() {
        if (continentSelect.getValue().isEmpty()) {
            return null;
        }
        return Continent.valueOf(continentSelect.getValue().toUpperCase());
    }

    /**
     * Gets an option for given gender
     * @param gender - target gender
     * @return - Selenide element <option>
     */
    @SneakyThrows
    private SelenideElement getGenderOption(Gender gender) {
        for (SelenideElement option : genderOptions) {
            if (option.getValue().equals(gender.getValue())) {
                return option;
            }
        }
        throw new UiEngineExecutionException("Unable to find gender option for \"" + gender + "\"");
    }

    /**
     * Gets a radio button for given color
     * @param color - target color
     * @return Selenide element <input type=radio>
     */
    @SneakyThrows
    private SelenideElement getColorElement(Color color) {
        for (SelenideElement element : colors) {
            if (element.getAttribute("id").equalsIgnoreCase(color.toString().toLowerCase())) {
                return element;
            }
        }
        throw new UiEngineExecutionException("Unable to find color radio button for \"" + color + "\"");
    }

    /**
     * Gets an option element for given continent
     * @param continent - target continent
     * @return - Selenide element <option>
     */
    @SneakyThrows
    private SelenideElement getContinentOption(Continent continent) {
        for (SelenideElement option : continentOptions) {
            if (continent.toString().equalsIgnoreCase(option.getValue())) {
                return option;
            }
        }
        throw new UiEngineExecutionException("Unable to find continent option for \"" + continent + "\"");
    }

    /**
     * Gets a checkbox for given contact type
     * @param contact - given contact
     * @return - Selenide element <input type=checkbox>
     */
    @SneakyThrows
    private SelenideElement getContactCheckbox(Contact contact) {
        for (SelenideElement checkbox : contacts) {
            if (checkbox.getAttribute("id").equals(contact.getId())) {
                return checkbox;
            }
        }
        throw new UiEngineExecutionException("Unable to find contact checkbox for \"" + contact + "\"");
    }
}

package com.vezh.lab.ui.example;

import com.vezh.lab.core.matcher.ExampleMatcher;
import com.vezh.lab.ui.UiTestCore;
import com.vezh.lab.ui.form.example.*;
import com.vezh.lab.ui.pages.example.ExamplePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Epic("Example")
@Feature("Example UI test")
public class ExampleTest extends UiTestCore {

    @Autowired
    private ExamplePage page;

    @Autowired
    private ExampleMatcher matcher;

    @Severity(SeverityLevel.CRITICAL)
    @Tag("UI")
    @DisplayName("Example test")
    @Test
    public void exampleTest() {
        ExampleForm form = new ExampleForm()
                .setFirstName("Automatic")
                .setSurname("Test")
                .setGender(Gender.MALE)
                .setColor(Color.RED)
                .addContact(Contact.EMAIL, Contact.SMS)
                .setMoreInfo("This is en example autotest " + RandomStringUtils.randomAlphanumeric(5))
                .setContinent(Continent.EUROPE);

        page
                .load()
                .fillThePage(form);
        matcher.assertExamplePageIsFilled(form, page);

        page.clickSubmitButton();
        matcher.assertPageIsBlank(page);
    }
}

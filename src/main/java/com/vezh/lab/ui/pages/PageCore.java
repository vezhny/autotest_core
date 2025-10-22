package com.vezh.lab.ui.pages;

import com.codeborne.selenide.*;
import com.vezh.lab.core.config.ui.SelenideConfig;
import com.vezh.lab.core.exception.UiEngineExecutionException;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class PageCore {

    @Autowired
    protected SelenideConfig selenideConfig;

    /**
     * Page loaded awaiting
     */
    public abstract PageCore load();

    /**
     * Returns true if the current page is opened
     * false otherwise
     */
    public abstract Boolean isCurrent();

    /**
     * Make a nice map of complicated attribute
     * Style, for example
     * @param element - target web element
     * @param attributeName - the name of complicated attribute
     */
    protected Map<String, String> getSubAttributesMap(SelenideElement element, String attributeName) {
        String attributesString = element.getAttribute(attributeName);
        String[] attributes = attributesString.split(";");
        Map<String, String> attributesMap = new HashMap<>();
        for (String attribute : attributes) {
            String[] keyAndValue = attribute.split(":");
            attributesMap.put(keyAndValue[0].trim(), keyAndValue[1].trim());
        }
        return attributesMap;
    }

    /**
     * Simply gets one style attribute of the element
     * @param element - target element
     * @param attributeName - style attribute name
     * @return
     */
    protected String getStyleAttribute(SelenideElement element, String attributeName) {
        return getSubAttributesMap(element, "style").get(attributeName);
    }

    /**
     * Gets list of classes in element
     * @param element - target web element
     * @return
     */
    protected List<String> getClasses(SelenideElement element) {
        return Arrays.asList(element.getAttribute("class").split("\\s+"));
    }

    /**
     * Refreshes page
     * @return
     */
    @Step("Refresh page")
    public PageCore refresh() {
        Selenide.refresh();
        return this;
    }

    /**
     * Checks or unchecks checkbox
     * @param checkbox - checkbox element
     * @param checked - required condition
     * @return
     */
    protected PageCore setCheckboxCheckedTo(SelenideElement checkbox, boolean checked) {
        if (checkbox.has(Condition.checked) != checked) {
            checkbox.click();
        }
        return this;
    }

    /**
     * Clears text field by sending CTRL+A and BACKSPACE
     * @param textField - target text field
     */
    protected PageCore clearTextField(SelenideElement textField) {
        textField.sendKeys(Keys.CONTROL + "A");
        textField.sendKeys(Keys.BACK_SPACE);
        return this;
    }

    /**
     * Scrolls up to the end
     * @return
     */
    public PageCore scrollUp() {
        Selenide.actions().sendKeys(Keys.HOME);
        return this;
    }

    /**
     * Scrolls down to the end
     * @return
     */
    public PageCore scrollDown() {
        Selenide.actions().sendKeys(Keys.END);
        return this;
    }

    /**
     * Scrolls to target element
     * @param element - target element to scroll on
     * @return
     */
    public PageCore scrollTo(SelenideElement element) {
        element.scrollIntoView("{behavior: \"instant\", block: \"center\", inline: \"nearest\"}");
        return this;
    }

    /**
     * Presses key on web element
     * @param selenideElement - target web element
     * @param keys - pressing key
     * @return
     */
    @Step("Press key {1}")
    protected PageCore pressKey(SelenideElement selenideElement, Keys keys) {
        selenideElement.sendKeys(keys);
        return this;
    }

    /**
     * Checks element's condition with timeout
     * Simple wrapper
     * @param element - target element
     * @param condition - expected condition
     */
    protected void checkCondition(SelenideElement element, WebElementCondition condition) {
        element.shouldBe(condition, Duration.ofMillis(selenideConfig.getTimeout()));
    }

    /**
     * Check element's visibility
     * @param element - target element
     */
    protected void checkVisible(SelenideElement element) {
        checkCondition(element, Condition.visible);
    }

    /**
     * Checks elements collection condition with timeout
     * Simple wrapper
     * @param elements - target elements
     * @param condition - expected condition
     */
    protected void checkCondition(ElementsCollection elements, WebElementsCondition condition) {
        elements.shouldBe(condition, Duration.ofMillis(selenideConfig.getTimeout()));
    }

    /**
     * Gets element, that contains text inside
     * No matter of registry
     * Returns null, if no such element found
     * @param elements - elements
     * @param targetText - text to search for
     * @return
     */
    protected SelenideElement getElementWithTextInside(ElementsCollection elements, String targetText) {
        for (SelenideElement element : elements) {
            if (StringUtils.containsIgnoreCase(element.getText(), targetText)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Searches a first Selenide element, where it's attribute matches to value
     * Returns null, if there is no such element
     * ! Be aware of NPE !
     * @param elements - elements collection to search at
     * @param attributeName - attribute name to check
     * @param targetValue - target value
     * @return
     */
    protected SelenideElement searchElementWithAttribute(ElementsCollection elements, String attributeName,
                                                         String targetValue) {
        for (SelenideElement element : elements) {
            if (element.getAttribute(attributeName).equalsIgnoreCase(targetValue)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Searches a first Selenide element, where it's attribute matches to value
     * Throws an exception, if there is no such element
     * @param elements - elements collection to search at
     * @param attributeName - attribute name to check
     * @param targetValue - target value
     * @return
     */
    @SneakyThrows
    protected SelenideElement getElementWithAttribute(ElementsCollection elements, String attributeName,
                                                      String targetValue) {
        SelenideElement element = searchElementWithAttribute(elements, attributeName, targetValue);
        if (element == null) {
            throw new UiEngineExecutionException("Unable to get element with \"" + attributeName + " = " + targetValue + "\"");
        }
        return element;
    }
}

package com.vezh.lab.core.matcher;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Actually, there is no big need of most of these methods
 * But they are covered with Allure to have a pretty report
 */
@Component
public abstract class MatcherCore {

    /**
     * Simple covering of JUnit assertEquals
     * Needed to show assertion in Allure report
     * @param expected - expected object
     * @param actual - actual object
     * @param checkingValue - name of the value, that checking
     */
    @Step("Assert {2} are equals")
    public void assertEquals(Object expected, Object actual, String checkingValue) {
        Assertions.assertEquals(expected, actual, checkingValue + " mismatch");
    }

    /**
     * Simple covering of JUnit assertTrue
     * Needed to fix assertion in Allure report
     * @param actual - actual condition
     * @param checkingValue - name of the value, that checking
     */
    @Step("Assert that {1} is true")
    public void assertTrue(Boolean actual, String checkingValue) {
        Assertions.assertTrue(actual, checkingValue + " should be true");
    }

    /**
     * Simple covering of JUnit assertFalse
     * Needed to fix assertion in Allure report
     * @param actual - actual condition
     * @param checkingValue - name of the value, that checking
     */
    @Step("Assert that {1} is false")
    public void assertFalse(Boolean actual, String checkingValue) {
        Assertions.assertFalse(actual, checkingValue + " should be false");
    }

    /**
     * Asserts server response code
     * @param expectedCode - expected response code
     * @param actualResponse - actual response from server
     */
    public void assertResponseCode(HttpStatus expectedCode, Response actualResponse) {
        assertEquals(expectedCode, actualResponse.getStatusCode(), "response codes");
    }

    /**
     * Asserts response code is not fail
     * @param actualResponse - actual response from server
     */
    @Step("Assert success response")
    public void assertSuccessResponse(Response actualResponse) {
        assertAnyValueOf(actualResponse.getStatusCode(), "response code",
                HttpStatus.SC_OK,
                HttpStatus.SC_CREATED,
                HttpStatus.SC_NO_CONTENT
        );
    }

    /**
     * Asserts, that response means failure
     * @param response - actual response from server
     */
    @Step("Assert fail response")
    public void assertFailResponse(Response response) {
        assertAnyValueOf(response.getStatusCode(), "response code",
                HttpStatus.SC_NOT_FOUND,
                HttpStatus.SC_BAD_REQUEST,
                HttpStatus.SC_FORBIDDEN,
                HttpStatus.SC_UNAUTHORIZED
        );
    }

    /**
     * Asserts that specified value presents
     * @param value - target value
     * @param valueName - target value name
     */
    @Step("Assert that value \"{1}\" exists")
    public void assertValueExists(Object value, String valueName) {
        assertThat("Value \"" + valueName + "\" exists", value, notNullValue());
    }

    /**
     * Asserts that specified value absent
     * @param value - target value
     * @param valueName - target value name
     */
    @Step("Assert that value \"{1}\" absent")
    public void assertValueAbsent(Object value, String valueName) {
        assertThat("Value \"" + valueName + "\" absent", value, nullValue());
    }

    /**
     * Asserts value equals or null
     * @param expected - expected value
     * @param actual - actual value
     * @param valueName - assserting value name
     */
    @Step("Assert {valueName} is absent or null")
    public void assertEqualsOrNull(Object expected, Object actual, String valueName) {
        if (expected == null) {
            assertValueAbsent(actual, valueName);
        } else {
            assertEquals(expected, actual, valueName);
        }
    }

    /**
     * Asserts exception
     * @param expectedExceptionClass - expected exception class
     * @param executable - function to call exception
     * @param message - showing message
     */
    @Step("Assert {2}")
    public void assertException(Class expectedExceptionClass, Executable executable, String message) {
        assertThrows(expectedExceptionClass, executable, message);
    }

    /**
     * Asserts, that two string values equals
     * Case is ignoring
     * @param expected - expected value
     * @param actual - actual value
     * @param valueName - value name
     */
    @Step("Assert value \"{2}\" equals ignoring case")
    public void assertEqualsIgnoreCase(String expected, String actual, String valueName) {
        assertThat(valueName + " matches ignoring case", actual, equalToIgnoringCase(expected));
    }

    /**
     * Asserts, that array empty or null
     * @param objects - target array
     * @param valueName - array's name
     */
    @Step("Assert, that there is no {1}")
    public void assertArrayEmptyOrNull(Object[] objects, String valueName) {
        assertThat(valueName + " array empty or null", objects,
                anyOf(emptyArray(), nullValue()));
    }

    /**
     * Asserts, that response from server is an empty array
     * @param response - response from server
     * @param valueName - array's name
     */
    @Step("Assert, that there is no {1}")
    public void assertArrayEmpty(Response response, String valueName) {
        assertThat(valueName + " array empty or null", response.as(Object[].class), is(emptyArray()));
    }

    /**
     * Asserts, that list empty or null
     * @param objects - target list
     * @param valueName - list's name
     */
    public void assertListEmptyOrNull(List objects, String valueName) {
        assertArrayEmptyOrNull(objects.toArray(), valueName);
    }

    /**
     * Asserts, that array has item
     * @param objects - target array
     * @param value - target item
     * @param description - case description
     */
    @Step("Assert, that {2}")
    public void assertValueInArray(Object[] objects, Object value, String description) {
        assertThat(description, objects, hasItemInArray(value));
    }

    /**
     * Asserts, that list has item
     * @param objects - target list
     * @param value - target item
     * @param description - case description
     */
    public void assertValueInList(List objects, Object value, String description) {
        assertValueInArray(objects.toArray(), value, description);
    }

    /**
     * Asserts empty or ull array in response body
     * @param response - actual response
     * @param valueName - array name
     */
    public void assertEmptyArrayInResponse(Response response, String valueName) {
        assertArrayEmptyOrNull(response.as(Object[].class), valueName);
    }

    /**
     * Asserts, that array has required items
     * @param actualArray - actual array
     * @param arrayName - actual array name
     * @param expectedItems - expected items inside
     */
    @Step("Assert, that {1} has all {2}")
    public void assertArrayHas(Object[] actualArray, String arrayName, Object... expectedItems) {
        for (Object expectedItem : expectedItems) {
            assertValueInArray(actualArray, expectedItem, arrayName + " has " + expectedItem.toString());
        }
    }

    /**
     * Asserts, that list has required items
     * @param actualObjects - actual objects
     * @param listName - actual list name
     * @param expectedItems - expected items inside
     */
    @Step("Assert, that {1} has all {2}")
    public void assertListHas(List actualObjects, String listName, Object... expectedItems) {
        for (Object expectedItem : expectedItems) {
            assertValueInList(actualObjects, expectedItem, listName + " has " + expectedItem.toString());
        }
    }

    /**
     * Asserts, that value is any of acceptable ones
     * @param actualValue - actual value
     * @param valueName - value name
     * @param acceptableValues - acceptable values
     */
    @Step("Assert, that {1} is any of {2}")
    public void assertAnyValueOf(Object actualValue, String valueName, Object... acceptableValues) {
        assertThat(valueName + " is any of " + acceptableValues, actualValue, Matchers.oneOf(acceptableValues));
    }

    /**
     * Asserts sorting of list
     * @param objects - target list
     * @param comparator - comparator, of how it should be sorted
     * @param listName - target list's name
     */
    @Step("Assert, that {listName} is sorted")
    public void assertSort(List objects, Comparator comparator, String listName) {
        List expectedObjects = new ArrayList();
        expectedObjects.addAll(objects);
        Collections.sort(expectedObjects, comparator);
        assertEquals(expectedObjects, objects, listName + " sorting");
    }

    /**
     * Asserts, that given value is close to acceptable one
     * @param expectedValue - expected value
     * @param actualValue - actual value
     * @param acceptableDelta - acceptable difference with expected value
     * @param valueName - value name
     */
    @Step("Assert, that {valueName} is about {expectedValue} +-{acceptableDelta}")
    public void assertValueIsAbout(Integer expectedValue, Integer actualValue, Integer acceptableDelta, String valueName) {
        MatcherAssert.assertThat(valueName + " is about " + expectedValue + " +- " + acceptableDelta,
                actualValue,
                Matchers.allOf(Matchers.greaterThanOrEqualTo(expectedValue - acceptableDelta),
                        Matchers.lessThanOrEqualTo(expectedValue + acceptableDelta)));
    }

    /**
     * Asserts, that given value is in acceptable range
     * @param acceptableRange - given acceptable range in format:
     *                        [0] - minimal value
     *                        [1] - maximal value
     * @param actualValue - actual value
     * @param valueName - value name
     */
    @Step("Assert, that {valueName} is about {expectedValue} +-{acceptableDelta}")
    public void assertValueIsAbout(Integer[] acceptableRange, Integer actualValue, String valueName) {
        MatcherAssert.assertThat(valueName + " is about [" + acceptableRange[0] + "; " + acceptableRange[1] + "]",
                actualValue,
                Matchers.allOf(Matchers.greaterThanOrEqualTo(acceptableRange[0]),
                        Matchers.lessThanOrEqualTo(acceptableRange[1])));
    }

    /**
     * Asserts equality of two date&time values with truncation
     * @param expected - expected value
     * @param actual - actual value
     * @param truncation - truncation
     * @param valueName - value name
     */
    public void assertChronoValuesEquals(LocalDateTime expected, LocalDateTime actual, ChronoUnit truncation,
                                         String valueName) {
        assertEquals(expected.truncatedTo(truncation), actual.truncatedTo(truncation), valueName);
    }

    /**
     * Asserts, that given value is numeric
     * @param value - given value
     * @param valueName - the name of given value
     */
    @Step("Assert, that {valueName} is numeric")
    public void assertValueIsNumeric(String value, String valueName) {
        assertPattern(value, "\\d+", valueName);
    }

    /**
     * Asserts, that given value matches target pattern
     * @param value - given value
     * @param pattern - target pattern
     * @param valueName - the name of given value
     */
    @Step("Assert, that {valueName} matches pattern \"{pattern}\"")
    public void assertPattern(String value, String pattern, String valueName) {
        MatcherAssert.assertThat(valueName + " matches pattern \"" + pattern + "\"", value,
                Matchers.matchesPattern(pattern));
    }
}

package com.vezh.lab.api.example;

import com.vezh.lab.api.ApiTestCore;
import com.vezh.lab.api.http.example.ExampleApi;
import com.vezh.lab.core.matcher.ExampleMatcher;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Epic("Example")
@Feature("Example API test")
public class ExampleTest extends ApiTestCore {

    @Autowired
    private ExampleApi api;

    @Autowired
    private ExampleMatcher matcher;

    @Severity(SeverityLevel.CRITICAL)
    @Link(value = "https://beeceptor.com/docs/sample-api-for-testing/")
    @Tag("API")
    @DisplayName("Example test")
    @Test
    public void exampleTest() {
        Response response = api.sendExample();

        matcher.assertSuccessResponse(response);
        matcher.assertExampleModels(response);
    }
}

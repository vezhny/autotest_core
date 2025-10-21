package com.vezh.lab.core.matcher;

import com.vezh.lab.api.model.example.ExampleModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

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
}

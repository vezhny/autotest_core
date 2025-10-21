package com.vezh.lab.api.http.example;

import com.vezh.lab.api.http.ApiCore;
import com.vezh.lab.api.http.Endpoints;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class ExampleApi extends ApiCore {

    /**
     * Sends an example API call
     * @return - RestAssured response instance
     */
    @Step("Send an example request")
    public Response sendExample() {
        Response response = sendRequest(Endpoints.EXAMPLE, Method.GET);
        return response;
    }
}

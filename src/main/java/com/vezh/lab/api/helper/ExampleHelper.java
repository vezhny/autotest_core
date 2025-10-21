package com.vezh.lab.api.helper;

import com.vezh.lab.api.http.example.ExampleApi;
import com.vezh.lab.api.model.example.ExampleModel;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ExampleHelper extends CoreHelper {

    @Autowired
    private ExampleApi api;

    /**
     * Sends an example request
     * Checks response code
     * @return - example models list
     */
    public List<ExampleModel> sendExampleRequest() {
        Response response = api.sendExample();
        checkResponse(response);
        List<ExampleModel> exampleModels = Arrays.asList(response.as(ExampleModel[].class));
        return exampleModels;
    }
}

package com.vezh.lab.api.helper;

import com.vezh.lab.core.exception.ApiEngineExecutionException;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public abstract class CoreHelper {

    /**
     * Checks response code
     * Throws an exception, if response is not success
     * @param response - target RestAssured response instance
     */
    @SneakyThrows
    public void checkResponse(Response response) {
        if (response.getStatusCode() != HttpStatus.SC_OK
                && response.getStatusCode() != HttpStatus.SC_CREATED
                && response.getStatusCode() != HttpStatus.SC_NO_CONTENT) {
            throw new ApiEngineExecutionException("Server returned response code \"" + response.getStatusCode() + "\"");
        }
    }
}

package com.vezh.lab.api.http;

import com.vezh.lab.api.json.JsonSerializer;
import com.vezh.lab.core.config.api.ApiConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
@Slf4j
public abstract class ApiCore {

    @Autowired
    protected ApiConfig apiConfig;

    @Setter
    private String token;

    @Setter
    private Boolean authenticate = true;

    @Setter
    private Boolean rawBody = false;

    @Setter
    @Getter
    private Boolean restoreTokenAfterRequest = false;

    @Setter
    private Boolean attachResponseBody = true;
    
    /**
     * Base RestAssured instance initialization
     * @return RestAssured instance
     */
    protected RequestSpecification defaultSpec() {
        RequestSpecification requestSpecification = given()
                .baseUri(apiConfig.getUrl());
//                .filter(new AllureRestAssured())
//                .contentType("application/json;charset=UTF-8"); uncomment to specify content-type
        if (authenticate) {
            // authentication with Bearer token
            requestSpecification = requestSpecification.header("Authorization",
                    "Bearer " + getToken());
        }
        if (apiConfig.getTrustSsl()) {
            requestSpecification
                    .config(RestAssuredConfig.newConfig().sslConfig(new SSLConfig().relaxedHTTPSValidation()));
        }
        return requestSpecification;
    }

    /**
     * Sends requests, using base RestAssured instance
     * @param endpoint - feature URL
     * @param body - request body
     * @param restMethod - using REST-method
     * @return - server response
     */
    protected Response sendRequest(String endpoint, Object body, Method restMethod) {
        RequestSpecification requestSpecification = defaultSpec().contentType(ContentType.JSON);
        return sendRequest(requestSpecification, endpoint, body, restMethod);
    }

    /**
     * Sends requests without body, using base RestAssured instance
     * @param endpoint - feature URL
     * @param restMethod - using REST-method
     * @return - server response
     */
    protected Response sendRequest(String endpoint, Method restMethod) {
        RequestSpecification requestSpecification = defaultSpec().contentType(ContentType.JSON);
        return sendRequest(requestSpecification, endpoint, null, restMethod);
    }

    /**
     * Sends requests without body using custom RestAssured instance
     * @param requestSpecification - custom RestAssured instance
     * @param endpoint - feature URL
     * @param restMethod - using REST-method
     * @return - server response
     */
    protected Response sendRequest(RequestSpecification requestSpecification, String endpoint, Method restMethod) {
        return sendRequest(requestSpecification, endpoint, null, restMethod);
    }

    /**
     * Sends requests, using custom RestAssured instance
     * @param requestSpecification - custom RestAssured instance
     * @param endpoint - feature URL
     * @param body - request body
     * @param restMethod - using REST-method
     * @return - server response
     */
    @Step("Send {3} request")
    protected Response sendRequest(RequestSpecification requestSpecification, String endpoint, Object body, Method restMethod) {
//        logRequest(requestSpecification, restMethod, endpoint);

        requestSpecification = getRequestBody(body, requestSpecification);
        requestSpecification.log().method();
        requestSpecification.log().uri();
        requestSpecification.log().parameters();
        requestSpecification.log().body();

        Response response = null;
        switch (restMethod) {
            case GET: {
                response = requestSpecification.get(endpoint);
                break;
            }
            case POST: {
                response = requestSpecification.post(endpoint);
                break;
            }
            case PUT: {
                response = requestSpecification.put(endpoint);
                break;
            }
            case DELETE: {
                response = requestSpecification.delete(endpoint);
                break;
            }
            case PATCH: {
                response = requestSpecification.patch(endpoint);
                break;
            }
        }

//        String responseBody = JsonSerializer.formatJson(response.body().asString());
//        long responseLineCount = responseBody.lines().count();
//        log.info("Response code: " + response.getStatusCode());
//        if (responseLineCount > apiProperties.getInfoLogLimit()) {
//            if (responseLineCount > apiProperties.getDebugLogLimit()) {
//                log.info("Response body is extremely long (over " + apiProperties.getDebugLogLimit() + " lines). " +
//                        "Will be skipped from logs to save disk space");
//            } else {
//                log.info("Response body is too long (over " + apiProperties.getInfoLogLimit() + " lines). " +
//                        "Will be printed in debug log only");
//                log.debug("Response body:\n" + responseBody);
//            }
//        } else {
//            log.info("Response body:\n" + responseBody);
//        }
        return response;
    }

    /**
     * Logs http-request
     * Query parameters included
     * @param requestSpecification - RestAssured construction
     * @param restMethod - Selected REST-method
     * @param url - absolute url
     */
    private void logRequest(RequestSpecification requestSpecification, Method restMethod, String url) {
        RequestSpecificationImpl specImpl = (RequestSpecificationImpl) requestSpecification;
        String absoluteUrl = specImpl.getURI(specImpl.partiallyApplyPathParams(url, false, null));
        Allure.addAttachment("URL", absoluteUrl);
        log.info(restMethod.toString() + ":\t" + absoluteUrl);
        if (!specImpl.getRequestParams().isEmpty()) {
            log.info("PARAMS: " + specImpl.getRequestParams());
        }
    }

    /**
     * Makes JSON from object
     * Adds json-string to request's body
     * If object is null, all actions are ignoring and RestAssured instance with return without changes
     * @param body - object for JSON-serialization
     * @param requestSpecification - RestAssured instance
     * @return - RestAssured instance with body
     */
    private RequestSpecification getRequestBody(Object body, RequestSpecification requestSpecification) {
        if (body != null) {
            if (rawBody) {
                requestSpecification.body(body);
//                log.info("Request body:\n" + body);
                Allure.addAttachment("Request body", "text/plain", body.toString());
//                setRawBody(false);
            } else {
                String json = JsonSerializer.getJson(body);
                requestSpecification.body(json);
                json = JsonSerializer.formatJson(json);
//                log.info("Request body:\n" + json);
                Allure.addAttachment("Request body", "application/json", json);
            }
        }
        return requestSpecification;
    }

    /**
     * Returns authentication token
     * Gets a new one, if it's absent
     * @return
     */
    private String getToken() {
        if (token == null) {
            // TODO: getting bearer token here and set it to 'token' variable
        }
        return token;
    }
}

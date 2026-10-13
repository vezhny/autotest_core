package com.vezh.lab.api.http;

import com.vezh.lab.core.config.api.ApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/**
 * Keeps authentication token, shared between all API classes
 * Token is refreshed, when it's absent or expired (api.tokenTimeout)
 * Token can be temporary replaced for current thread with runAs(...)
 */
@Component
@Slf4j
public class TokenStore {

    @Autowired
    private ApiConfig apiConfig;

    private final ThreadLocal<String> temporaryToken = new ThreadLocal<>();

    private String token;
    private Instant tokenDate;

    /**
     * Returns token
     * Temporary token (see runAs) has priority over the stored one
     * If stored token is absent or expired, gets a new one
     * @param authenticator - gets a new token
     * @return - token
     */
    public String getToken(Supplier<String> authenticator) {
        String temporary = temporaryToken.get();
        if (temporary != null) {
            return temporary;
        }
        synchronized (this) {
            if (token == null || isExpired()) {
                log.info("Getting new token");
                setToken(authenticator.get());
            }
            return token;
        }
    }

    /**
     * Replaces stored token
     * @param token - new token
     */
    public synchronized void setToken(String token) {
        this.token = token;
        this.tokenDate = token == null ? null : Instant.now();
    }

    /**
     * Removes stored token. A new one will be got on the next request
     */
    public void reset() {
        setToken(null);
    }

    /**
     * Performs action with given token in current thread, then returns previous token back
     * @param token - token for action
     * @param action - action (e.g. API request)
     * @return - action result
     */
    public <T> T runAs(String token, Supplier<T> action) {
        String previous = temporaryToken.get();
        temporaryToken.set(token);
        try {
            return action.get();
        } finally {
            if (previous == null) {
                temporaryToken.remove();
            } else {
                temporaryToken.set(previous);
            }
        }
    }

    /**
     * Performs action with given token in current thread, then returns previous token back
     * @param token - token for action
     * @param action - action (e.g. API request)
     */
    public void runAs(String token, Runnable action) {
        runAs(token, () -> {
            action.run();
            return null;
        });
    }

    private boolean isExpired() {
        Duration timeout = apiConfig.getTokenTimeout();
        return timeout != null && tokenDate.plus(timeout).isBefore(Instant.now());
    }
}

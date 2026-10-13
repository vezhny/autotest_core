package com.vezh.lab.core.config;

/**
 * Secrets are not stored in git: they come either from ~/.autotest_core/secrets.yml (see secrets.example.yml
 * in the project root) or from environment variables, e.g. API_PASSWORD for api.password
 * Checked on use, so a test that doesn't need a secret runs without it
 * Usage: RequiredSecret.require(apiConfig.getPassword(), "api.password")
 */
public final class RequiredSecret {

    public static final String SECRETS_FILE = "~/.autotest_core/secrets.yml";

    private RequiredSecret() {
    }

    /**
     * Returns the secret or fails with a hint, where to put it
     * @param value - bound property value
     * @param property - property name, e.g. "api.password"
     * @return - the secret
     */
    public static String require(String value, String property) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Secret \"" + property + "\" is not set. Put it into "
                    + SECRETS_FILE + " (see secrets.example.yml) or set environment variable "
                    + toEnvironmentVariable(property));
        }
        return value;
    }

    /**
     * Spring relaxed binding name of the property, e.g. api.password -> API_PASSWORD
     * @param property - property name
     * @return - environment variable name
     */
    private static String toEnvironmentVariable(String property) {
        return property.replace('.', '_').replace("-", "").toUpperCase();
    }
}

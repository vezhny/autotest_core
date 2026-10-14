package com.vezh.lab.core.log;

import java.util.regex.Pattern;

/**
 * Masks sensitive values (passwords, tokens, api keys) in text before it gets into logs and Allure report
 * A value is sensitive, if its key ends with one of SENSITIVE words (case-insensitive), e.g.
 * password, newPassword, user_pwd, token, refresh_token, clientSecret, X-Api-Key, Cookie
 * So tokenTimeout or tokenType are not masked
 */
public final class LogMasker {

    public static final String MASK = "*****";

    private static final String SENSITIVE = "(?:password|passwd|pwd|token|secret|api[_-]?key|cookie)";

    private static final String KEY = "[\\w-]*" + SENSITIVE;

    // true, false and null are not masked, so it's still seen, that e.g. token is absent
    private static final String NOT_LITERAL = "(?!(?:true|false|null)\\b)";

    // "password": "secret" or "password": 12345
    private static final Pattern JSON = Pattern.compile(
            "(\"" + KEY + "\"\\s*:\\s*)" + NOT_LITERAL + "(\"(?:[^\"\\\\]|\\\\.)*\"|[^,}\\s]+)",
            Pattern.CASE_INSENSITIVE);

    // <password>secret</password>
    private static final Pattern XML = Pattern.compile(
            "(<(" + KEY + ")>)[^<]*(</\\2>)", Pattern.CASE_INSENSITIVE);

    // password=secret (query, form params, lombok toString) or password: secret
    private static final Pattern KEY_VALUE = Pattern.compile(
            "(\\b" + KEY + "\\s*[=:]\\s*)" + NOT_LITERAL + "([^&\\s,;)\"]+)", Pattern.CASE_INSENSITIVE);

    // Authorization header value
    private static final Pattern AUTHORIZATION = Pattern.compile(
            "\\b((?:Bearer|Basic)\\s+)[\\w.~+/=-]+", Pattern.CASE_INSENSITIVE);

    private static final Pattern SENSITIVE_NAME = Pattern.compile(KEY, Pattern.CASE_INSENSITIVE);

    private LogMasker() {
    }

    /**
     * Replaces sensitive values in text with mask
     * @param text - text to mask (json, xml, url, Authorization header, object's toString())
     * @return - masked text
     */
    public static String mask(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String masked = JSON.matcher(text).replaceAll("$1\"" + MASK + "\"");
        masked = XML.matcher(masked).replaceAll("$1" + MASK + "$3");
        masked = KEY_VALUE.matcher(masked).replaceAll("$1" + MASK);
        return AUTHORIZATION.matcher(masked).replaceAll("$1" + MASK);
    }

    /**
     * Masks named value: fully, if the name is sensitive, otherwise as a text
     * @param name - value name, e.g. step parameter name
     * @param value - value to mask
     * @return - masked value
     */
    public static String mask(String name, String value) {
        if (value == null) {
            return null;
        }
        return isSensitive(name) ? MASK : mask(value);
    }

    /**
     * Checks, if value with such a name has to be masked
     * @param name - value name
     * @return - true, if sensitive
     */
    public static boolean isSensitive(String name) {
        return name != null && SENSITIVE_NAME.matcher(name).matches();
    }
}

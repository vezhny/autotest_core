package com.vezh.lab.core.log;

import java.util.regex.Pattern;

/**
 * Masks sensitive values (passwords) in text before it gets into logs
 * Key is sensitive if it contains one of: password, passwd, pwd (case-insensitive), e.g. password, newPassword, user_pwd
 */
public final class LogMasker {

    public static final String MASK = "*****";

    private static final String KEY = "[\\w-]*(?:password|passwd|pwd)[\\w-]*";

    // "password": "secret" or "password": 12345
    private static final Pattern JSON = Pattern.compile(
            "(\"" + KEY + "\"\\s*:\\s*)(\"(?:[^\"\\\\]|\\\\.)*\"|[^,}\\s]+)", Pattern.CASE_INSENSITIVE);

    // <password>secret</password>
    private static final Pattern XML = Pattern.compile(
            "(<(" + KEY + ")>)[^<]*(</\\2>)", Pattern.CASE_INSENSITIVE);

    // password=secret (query, form params) or password: secret
    private static final Pattern KEY_VALUE = Pattern.compile(
            "(\\b" + KEY + "\\s*[=:]\\s*)([^&\\s,;\"]+)", Pattern.CASE_INSENSITIVE);

    private LogMasker() {
    }

    /**
     * Replaces password values in text with mask
     * @param text - text to mask
     * @return - masked text
     */
    public static String mask(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String masked = JSON.matcher(text).replaceAll("$1\"" + MASK + "\"");
        masked = XML.matcher(masked).replaceAll("$1" + MASK + "$3");
        return KEY_VALUE.matcher(masked).replaceAll("$1" + MASK);
    }
}

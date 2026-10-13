package com.vezh.lab.core.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

/**
 * Drops Allure "Could not update test case" errors, which are not real problems and only litter the logs
 * Other AllureLifecycle messages are kept
 * Registered in logback-spring.xml
 */
public class AllureLifecycleFilter extends TurboFilter {

    private static final String LOGGER_NAME = "io.qameta.allure.AllureLifecycle";
    private static final String MESSAGE = "Could not update test case";

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        if (format != null && LOGGER_NAME.equals(logger.getName()) && format.startsWith(MESSAGE)) {
            return FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;
    }
}

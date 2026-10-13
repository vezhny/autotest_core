package com.vezh.lab.core.annotation;

import io.qameta.allure.LabelAnnotation;
import io.qameta.allure.LinkAnnotation;
import io.qameta.allure.util.ResultsUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks test with a known bug.
 * Adds an issue link (url is built from allure.link.issue.pattern) and a tag with ticket id into Allure report.
 * Replaces {@code @Link(type = "issue", value = "...")}
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@LinkAnnotation(type = ResultsUtils.ISSUE_LINK_TYPE)
@LabelAnnotation(name = ResultsUtils.TAG_LABEL_NAME)
public @interface Bug {

    /**
     * @return ticket id in a bug tracking system (e.g. JB-512)
     */
    String value();
}

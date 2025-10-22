package com.vezh.lab.ui;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.vezh.lab.TestCore;
import com.vezh.lab.core.config.ui.SelenideConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith({UiTestListener.class})
@Slf4j
public abstract class UiTestCore extends TestCore {

    @Autowired
    protected SelenideConfig selenideConfig;

    @BeforeAll
    public void openBrowser() {
        Selenide.open(selenideConfig.getUrl());
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    @AfterAll
    public void closeBrowser() {
        Selenide.closeWebDriver();
    }
}

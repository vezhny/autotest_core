package com.vezh.lab;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestCore {

    @BeforeEach
    public void beforeTest(TestInfo testInfo) {
        log.info("--------------- Start Test ---------------");
        log.info("TEST NAME: " + testInfo.getDisplayName());
        Allure.description(testInfo.getDisplayName());
    }

    @AfterEach
    public void afterTest() {
        log.info("--------------- End Test ---------------\n");
    }
}

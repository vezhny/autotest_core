package com.vezh.lab.common.report;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepListener implements StepLifecycleListener {

    /**
     * Writes step result to log
     * @param result - step result (allure)
     */
    @Override
    public void afterStepStart(StepResult result) {
        log.info(result.getName());
    }
}

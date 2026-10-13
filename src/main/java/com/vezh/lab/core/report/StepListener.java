package com.vezh.lab.core.report;

import com.vezh.lab.core.log.LogMasker;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepListener implements StepLifecycleListener {

    /**
     * Hides passwords and tokens in step parameters and step name (Allure)
     * e.g. "password" argument of @Step("Login as {login} with {password}")
     * or "body" argument with LoginRequest(login=x, password=y)
     * @param result - step result (allure)
     */
    @Override
    public void beforeStepStart(StepResult result) {
        String name = result.getName();
        for (Parameter parameter : result.getParameters()) {
            String value = parameter.getValue();
            if (name != null && value != null && !value.isEmpty() && LogMasker.isSensitive(parameter.getName())) {
                name = name.replace(value, LogMasker.MASK);
            }
            parameter.setValue(LogMasker.mask(parameter.getName(), value));
        }
        result.setName(LogMasker.mask(name));
    }

    /**
     * Writes step result to log
     * @param result - step result (allure)
     */
    @Override
    public void afterStepStart(StepResult result) {
        log.info(result.getName());
    }
}

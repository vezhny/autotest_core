package com.vezh.lab.core.config.ui;

import com.codeborne.selenide.Configuration;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("selenide")
@Data
public class SelenideConfig implements InitializingBean {

    private String url;
    private Long timeout;
    private Boolean screenshots;
    private String browser;
    private Boolean headless;

    @Override
    public void afterPropertiesSet() {
        Configuration.baseUrl = url;
        Configuration.timeout = timeout;
        Configuration.screenshots = screenshots;
        Configuration.browser = browser;
        Configuration.headless = headless;
    }
}

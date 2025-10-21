package com.vezh.lab.core.config.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api")
@Data
public class ApiConfig {

    private String url;
    private Integer port;
    private Boolean trustSsl;
}

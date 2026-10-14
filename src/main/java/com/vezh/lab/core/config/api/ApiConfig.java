package com.vezh.lab.core.config.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties("api")
@Data
public class ApiConfig {

    private String url;
    private Boolean trustSsl;
    // token lifetime, e.g. 30m. If absent, token never expires
    private Duration tokenTimeout;
    // uncomment for authentication; password is a secret, see secrets.example.yml
//    private String login;
//    private String password;
}

package com.ohdocha.cu.kprojectcu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "com.ohdocha.cu.kprojectcu")
public class Properties {

    private boolean debug;

    private String serverHost;
    private String serverName;
    private String serverVersion;

}

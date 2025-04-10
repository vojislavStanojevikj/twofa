package com.evlidevs.mk.twofa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qr")
public class QRCodeConfig {

    private int height;
    private int width;
    private String format;
    private String uri;
}

package com.evlidevs.mk.twofa.config;

import com.google.zxing.Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.binary.Base32;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

@Configuration
public class BeansConfig {

    @Bean
    public BinaryEncoder encoder() {
        return new Base32();
    }

    @Bean
    public RandomGenerator randomGenerator() {
        return new SecureRandom();
    }

    @Bean
    public Writer qrCodeWriter() {
        return new QRCodeWriter();
    }

}

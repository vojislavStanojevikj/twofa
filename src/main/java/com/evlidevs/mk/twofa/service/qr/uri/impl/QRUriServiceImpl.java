package com.evlidevs.mk.twofa.service.qr.uri.impl;

import com.evlidevs.mk.twofa.config.QRCodeConfig;
import com.evlidevs.mk.twofa.service.qr.secret.SecretGenerator;
import com.evlidevs.mk.twofa.service.qr.uri.QRUriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class QRUriServiceImpl implements QRUriService {

    private static final String BACKSLASH_REGEX = "\\+";
    private static final String BACKSLASH_REPLACEMENT = "%20";
    private static final String EMPTY = "";

    private final QRCodeConfig qrCodeConfig;
    private final SecretGenerator secretGenerator;

    @Override
    public String generateUri(String email) {
        return String.format(qrCodeConfig.getUri(), uriEncode(email),
                uriEncode(secretGenerator.generate()));
    }

    private String uriEncode(String text) {
        return Objects.isNull(text) ?  EMPTY:
                URLEncoder.encode(text, StandardCharsets.UTF_8)
                        .replaceAll(BACKSLASH_REGEX, BACKSLASH_REPLACEMENT);
    }
}
package com.evlidevs.mk.twofa.service.qr.secret.impl;

import com.evlidevs.mk.restframe.exception.base.InternalServerErrorException;
import com.evlidevs.mk.twofa.service.qr.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.BinaryEncoder;
import org.springframework.stereotype.Service;

import java.util.random.RandomGenerator;

import static com.evlidevs.mk.restframe.codes.InternalServerErrorCode.INTERNAL_SERVER_ERROR;
import static com.evlidevs.mk.restframe.util.ExceptionLoggerUtil.logAndSupplyException;


@Service
@RequiredArgsConstructor
@Slf4j
public class RandomSecretGeneratorImpl implements SecretGenerator {

    private static final int BYTE_SIZE = 20;

    private final RandomGenerator randomBytes;
    private final BinaryEncoder encoder;

    @Override
    public String generate() {
        try {
            return new String(encoder.encode(getRandomBytes()));
        } catch (Exception e) {
            throw logAndSupplyException("Error generating random secret", INTERNAL_SERVER_ERROR,
                    InternalServerErrorException::new).get();
        }
    }

    private byte[] getRandomBytes() {
        byte[] bytes = new byte[BYTE_SIZE];
        this.randomBytes.nextBytes(bytes);
        return bytes;
    }
}

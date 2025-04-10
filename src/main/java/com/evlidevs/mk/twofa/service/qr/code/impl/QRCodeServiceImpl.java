package com.evlidevs.mk.twofa.service.qr.code.impl;

import com.evlidevs.mk.restframe.exception.base.InternalServerErrorException;
import com.evlidevs.mk.twofa.config.QRCodeConfig;
import com.evlidevs.mk.twofa.service.qr.code.QRCodeService;
import com.evlidevs.mk.twofa.service.qr.uri.QRUriService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import static com.evlidevs.mk.restframe.codes.InternalServerErrorCode.INTERNAL_SERVER_ERROR;
import static com.evlidevs.mk.restframe.util.ExceptionLoggerUtil.logAndSupplyException;


@Service
@RequiredArgsConstructor
@Slf4j
public class QRCodeServiceImpl implements QRCodeService {

    private final QRCodeConfig qrCodeConfig;
    private final Writer qrCodeWriter;
    private final QRUriService qrUriService;


    @Override
    public byte[] generateQRCode(String email) {
        log.info("Generating QR code for email: {}", email);

        try (var pngOutputStream = new ByteArrayOutputStream()) {

            var bitMatrix = qrCodeWriter.encode(qrUriService.generateUri(email), BarcodeFormat.QR_CODE,
                    qrCodeConfig.getWidth(), qrCodeConfig.getHeight());

            MatrixToImageWriter.writeToStream(bitMatrix, qrCodeConfig.getFormat(), pngOutputStream);
            return pngOutputStream.toByteArray();

        } catch (Exception e) {
            throw logAndSupplyException(String.format("Error generating QR code for email: %s, reason: {%s}", email, e.getMessage()),
                    INTERNAL_SERVER_ERROR, InternalServerErrorException::new).get();
        }
    }
}
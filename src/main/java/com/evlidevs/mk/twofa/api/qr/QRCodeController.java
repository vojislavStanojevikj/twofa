package com.evlidevs.mk.twofa.api.qr;

import com.evlidevs.mk.restframe.validation.JakartaValidation;
import com.evlidevs.mk.twofa.service.qr.code.QRCodeService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class QRCodeController implements JakartaValidation {

    private final QRCodeService qrCodeService;

    @GetMapping(value = "/api/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<byte[]> generateQRCode(@RequestParam(required = false) @NotBlank(message = "Email Is Mandatory!") String email) {
        return Mono.fromCallable(() -> qrCodeService.generateQRCode(email));
    }
}

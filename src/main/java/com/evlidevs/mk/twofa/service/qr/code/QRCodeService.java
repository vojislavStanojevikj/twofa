package com.evlidevs.mk.twofa.service.qr.code;

import com.evlidevs.mk.restframe.validation.JakartaValidation;
import jakarta.validation.constraints.NotBlank;

public interface QRCodeService extends JakartaValidation {

    byte[] generateQRCode(@NotBlank(message = "Email is mandatory!") String email);

}

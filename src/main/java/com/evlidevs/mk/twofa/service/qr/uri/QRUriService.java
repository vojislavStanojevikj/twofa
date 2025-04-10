package com.evlidevs.mk.twofa.service.qr.uri;

import com.evlidevs.mk.restframe.validation.JakartaValidation;
import jakarta.validation.constraints.NotBlank;

public interface QRUriService extends JakartaValidation {

    String generateUri(@NotBlank(message = "Email is mandatory!") String email);

}

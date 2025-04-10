package com.evlidevs.mk.twofa.exception.handler;

import com.evlidevs.mk.restframe.dto.RestResponseDTO;
import com.evlidevs.mk.restframe.exception.base.BadRequestException;
import com.evlidevs.mk.restframe.exception.base.InternalServerErrorException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.evlidevs.mk.restframe.codes.InvalidParameterErrorCode.INVALID_PARAMETER;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DELIMITER = ", ";
    private static final String JAKARTA = "jakarta";

    @ExceptionHandler(InternalServerErrorException.class)
    public Mono<ResponseEntity<RestResponseDTO<Void>>> handleInternalServerErrorException(InternalServerErrorException ex) {
        return handleHttpStatusException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<RestResponseDTO<Void>>> handleBadRequestException(InternalServerErrorException ex) {
        return handleHttpStatusException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public Mono<ResponseEntity<RestResponseDTO<Void>>> handleConstraintViolation(ConstraintViolationException ex) {

        var errors = ex.getConstraintViolations().stream()
                .map(getViolationMessage())
                .collect(Collectors.toList());

        var restResponseDTO = new RestResponseDTO<Void>(HttpStatus.BAD_REQUEST.value(), Instant.now(), String.join(DELIMITER, errors), INVALID_PARAMETER.getCode());

        return Mono.just(new ResponseEntity<>(restResponseDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST));
    }

    private Function<ConstraintViolation<?>, String> getViolationMessage() {
        return violation -> {
            var violationPath = violation.getPropertyPath().toString().replace(".", " ");
            return isJakartaValidation(violation.getMessageTemplate())
                    ? String.format("%s %s", violationPath, violation.getMessage())
                    : violation.getMessage();
        };
    }

    private boolean isJakartaValidation(String messageTemplate) {
        return messageTemplate.contains(JAKARTA);
    }

    public Mono<ResponseEntity<RestResponseDTO<Void>>> handleHttpStatusException(InternalServerErrorException ex, HttpStatus status) {
        return Mono.just(new ResponseEntity<>(new RestResponseDTO<>(ex.getHttpStatus(), Instant.now(), ex.getMessage()), status));
    }

}
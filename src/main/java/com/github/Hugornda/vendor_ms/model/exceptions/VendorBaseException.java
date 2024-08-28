package com.github.Hugornda.vendor_ms.model.exceptions;

import lombok.Getter;
import org.springframework.graphql.execution.ErrorType;

@Getter
public class VendorBaseException extends Exception {

    private final ErrorType errorType;

    public VendorBaseException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

}

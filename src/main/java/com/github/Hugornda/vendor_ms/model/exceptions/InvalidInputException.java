package com.github.Hugornda.vendor_ms.model.exceptions;

import org.springframework.graphql.execution.ErrorType;

public class InvalidInputException extends VendorBaseException {

    public InvalidInputException(String message) {
        super(message, ErrorType.BAD_REQUEST);
    }
}

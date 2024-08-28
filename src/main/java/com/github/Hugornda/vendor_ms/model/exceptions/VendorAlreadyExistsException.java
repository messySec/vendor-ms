package com.github.Hugornda.vendor_ms.model.exceptions;

import org.springframework.graphql.execution.ErrorType;

public class VendorAlreadyExistsException extends VendorBaseException {

    public VendorAlreadyExistsException(String message) {
        super(message, ErrorType.BAD_REQUEST);
    }
}

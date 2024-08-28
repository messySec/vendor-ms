package com.github.Hugornda.vendor_ms.config.execption;

import com.github.Hugornda.vendor_ms.model.exceptions.InvalidInputException;
import com.github.Hugornda.vendor_ms.model.exceptions.VendorAlreadyExistsException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.concurrent.CompletableFuture;


@ControllerAdvice
public class VendorExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        GraphQLError graphQLError;
        Throwable exception = handlerParameters.getException();

        graphQLError = switch (exception) {
            case VendorAlreadyExistsException e -> getGraphqlError(e.getMessage(), e.getErrorType());
            case InvalidInputException e -> getGraphqlError(e.getMessage(),e.getErrorType());
            default -> getGraphqlError(exception.getMessage(), ErrorType.INTERNAL_ERROR);
        };
        DataFetcherExceptionHandlerResult exceptionHandlerResult = DataFetcherExceptionHandlerResult.newResult()
                .error(graphQLError)
                .build();
         return CompletableFuture.completedFuture(exceptionHandlerResult);
    }

    private static GraphQLError getGraphqlError(String message, ErrorType errorType) {
        return GraphqlErrorBuilder.newError()
                .message(message)
                .errorType(errorType)
                .build();
    }
}

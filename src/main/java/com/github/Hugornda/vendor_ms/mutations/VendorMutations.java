package com.github.Hugornda.vendor_ms.mutations;

import com.github.Hugornda.vendor_ms.model.Vendor;
import com.github.Hugornda.vendor_ms.model.exceptions.InvalidInputException;
import com.github.Hugornda.vendor_ms.service.VendorService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Mono;

@Slf4j
@DgsComponent
public class VendorMutations {

    private final VendorService vendorServiceImp;

    public VendorMutations( VendorService vendorServiceImp) {

        this.vendorServiceImp = vendorServiceImp;
    }

    @DgsMutation
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Vendor> createVendor(@InputArgument String name,
                                     @InputArgument Integer numberOfEmployees,
                                     @InputArgument String country) {
        String ERROR_MESSAGE = "Vendor %s can NOT be empty";
        if( name == null || name.isEmpty()) {
            return Mono.error(new InvalidInputException(String.format(ERROR_MESSAGE,"name")));
        }

        if( country == null || country.isEmpty()) {
            return Mono.error(new InvalidInputException(String.format(ERROR_MESSAGE,"country")));
        }

        return vendorServiceImp.createVendor(name,numberOfEmployees,country);
    }
}

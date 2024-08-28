package com.github.Hugornda.vendor_ms.mutations;

import com.github.Hugornda.vendor_ms.model.Vendor;
import com.github.Hugornda.vendor_ms.model.exceptions.InvalidInputException;
import com.github.Hugornda.vendor_ms.model.exceptions.VendorAlreadyExistsException;
import com.github.Hugornda.vendor_ms.repository.VendorRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Mono;

import static com.github.Hugornda.vendor_ms.utils.FormatterUtils.toJsonString;

@Slf4j
@DgsComponent
public class VendorMutations {

    private final VendorRepository vendorRepository;

    public VendorMutations(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
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

        Vendor vendor = new Vendor(name, numberOfEmployees, country);
        return vendorRepository.save(vendor)
                .doOnNext(res-> log.info("Created vendor: {}", toJsonString(res)))
                .onErrorResume(DuplicateKeyException.class, e -> Mono.error(
                        new VendorAlreadyExistsException("A vendor with this name already exists.")));
    }
}

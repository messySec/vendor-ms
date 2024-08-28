package com.github.Hugornda.vendor_ms.repository;

import com.github.Hugornda.vendor_ms.model.Vendor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface VendorRepository  extends ReactiveCrudRepository<Vendor, Long> {
    @NotNull
    Flux<Vendor> findAll();
}

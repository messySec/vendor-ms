package com.github.Hugornda.vendor_ms.service;

import com.github.Hugornda.vendor_ms.model.Vendor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VendorService {
    Flux<Vendor> findAll();
    Mono<Vendor> createVendor(String name , int numberOfEmployees, String country);
}

package com.github.Hugornda.vendor_ms.service;

import com.github.Hugornda.vendor_ms.model.Vendor;
import com.github.Hugornda.vendor_ms.model.exceptions.VendorAlreadyExistsException;
import com.github.Hugornda.vendor_ms.repository.VendorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;


@Service
public class VendorServiceImp implements VendorService {

    private final VendorRepository vendorRepository;
    private Sinks.Many<Vendor> vendorSink;
    private Flux<Vendor> vendorPublisher;

    public VendorServiceImp(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @PostConstruct
    private void createVendorPublisher() {
        vendorSink = Sinks.many().replay().all();
        Flux<Vendor> existingVendors = vendorRepository.findAll();
        vendorPublisher = vendorSink.asFlux().mergeWith(existingVendors).replay().autoConnect();
    }

    public Flux<Vendor> findAll() {
        return vendorPublisher;
    }

    @Override
    public Mono<Vendor> createVendor(String name, int numberOfEmployees, String country) {
        Vendor vendor = new Vendor(name, numberOfEmployees, country);
        return vendorRepository.save(vendor)
                .doOnNext(savedVendor -> vendorSink.tryEmitNext(savedVendor))
                .onErrorResume(DuplicateKeyException.class, e -> Mono.error(
                        new VendorAlreadyExistsException("A vendor with this name already exists.")));
    }
}

package com.github.Hugornda.vendor_ms;

import com.github.Hugornda.vendor_ms.model.Vendor;
import com.github.Hugornda.vendor_ms.repository.VendorRepository;
import com.github.Hugornda.vendor_ms.service.VendorServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
public class VendorServiceTests {

    @MockBean
    private VendorRepository vendorRepository;

    @Autowired
    private VendorServiceImp vendorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateVendor() {
        Vendor vendor = new Vendor("Vendor1", 100, "USA");

        when(vendorRepository.save(any(Vendor.class))).thenReturn(Mono.just(vendor));
        when(vendorRepository.findAll()).thenReturn(Flux.just(vendor));

        StepVerifier.create(vendorService.createVendor("Vendor1", 100, "USA"))
                .expectNextMatches(createdVendor ->
                        createdVendor.getName().equals("Vendor1") &&
                                createdVendor.getNumberOfEmployees() == 100 &&
                                createdVendor.getCountry().equals("USA")
                )
                .verifyComplete();

        StepVerifier.create(vendorService.findAll())
                .expectNext(vendor);
    }
}

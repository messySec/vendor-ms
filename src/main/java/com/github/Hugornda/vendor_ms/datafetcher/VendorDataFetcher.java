package com.github.Hugornda.vendor_ms.datafetcher;

import com.github.Hugornda.vendor_ms.model.Vendor;
import com.github.Hugornda.vendor_ms.repository.VendorRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import static com.github.Hugornda.vendor_ms.utils.FormatterUtils.toJsonString;

@DgsComponent
public class VendorDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(VendorDataFetcher.class);

    private final VendorRepository vendorRepository;

    public VendorDataFetcher(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @DgsQuery(field = "vendors")
    public Flux<Vendor> getVendors() {
        return vendorRepository.findAll()
                .doOnNext(res->log.info(toJsonString(res)));
    }
}

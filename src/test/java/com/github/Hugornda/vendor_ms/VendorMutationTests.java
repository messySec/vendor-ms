package com.github.Hugornda.vendor_ms;

import com.github.Hugornda.vendor_ms.mutations.VendorMutations;
import com.github.Hugornda.vendor_ms.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class VendorMutationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorMutations vendorMutations;

    private final String graphqlQuery = "{ \"query\": " +
            "\"mutation { " +
            "createVendor(name: \\\"Test vendor\\\", numberOfEmployees: 123, country: \\\"Portugal\\\") " +
            "{ name numberOfEmployees country } }\" }";

    private final String invalidGraphqlQuery = "{ \"query\": " +
            "\"mutation { " +
            "createVendor(name: \\\"\\\", numberOfEmployees: -1, country: \\\"\\\") " +
            "{ name numberOfEmployees country } }\" }";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateVendorWithAdminRole(){
        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(graphqlQuery)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.createVendor.name").isEqualTo("Test vendor");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateResourceMutationUnauthorized() {

        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(graphqlQuery)
                .exchange()
                .expectBody()
                .jsonPath("$.errors[0].message").isEqualTo("Forbidden")
                .jsonPath("$.data.createVendor").doesNotExist();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateVendorWithInvalidInput() {
        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidGraphqlQuery)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").isEqualTo("Vendor name can NOT be empty")
                .jsonPath("$.errors[0].extensions.classification").isEqualTo("BAD_REQUEST");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateVendorWithMissingFields() {
        String queryWithMissingFields = "{ \"query\": " +
                "\"mutation { " +
                "createVendor(name: \\\"Test vendor\\\", numberOfEmployees: 123) " +
                "{ name numberOfEmployees country } }\" }";

        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(queryWithMissingFields)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").isEqualTo("Vendor country can NOT be empty");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateDuplicateVendor() {

        String duplicateVendorQuery = "{ \"query\": " +
                "\"mutation { " +
                "createVendor(name: \\\"Duplicate vendor\\\", numberOfEmployees: 100, country: \\\"England\\\") " +
                "{ name numberOfEmployees country } }\" }";

        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(duplicateVendorQuery)
                .exchange();

        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(duplicateVendorQuery)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors[0].message").isEqualTo("A vendor with this name already exists.");

    }

}

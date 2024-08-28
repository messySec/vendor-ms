package com.github.Hugornda.vendor_ms;

import com.github.Hugornda.vendor_ms.repository.VendorRepository;
import com.github.Hugornda.vendor_ms.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class VendorDataFetcherTest {

    @Autowired
    private WebTestClient webTestClient;



    @Test
    public void getAllVendorsUnauthorized() throws IOException {
        String graphqlQuery = TestUtils.loadGraphQLQuery("graphql/getAllVendors.graphql");
        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(graphqlQuery)
                .exchange()
                .expectStatus().isUnauthorized();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllVendorsBadRequest() throws IOException {
        String invalidGraphqlQuery = TestUtils.loadGraphQLQuery("graphql/invalidgetall.graphql");
        webTestClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidGraphqlQuery)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors").exists();
    }

}

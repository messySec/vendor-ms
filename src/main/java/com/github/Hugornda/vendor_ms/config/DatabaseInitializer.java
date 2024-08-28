package com.github.Hugornda.vendor_ms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

@Configuration
public class DatabaseInitializer {


    @Bean
    public CommandLineRunner initializeDatabase(R2dbcEntityTemplate template) {
        return args -> {
            Mono<Void> schemaCreation = template.getDatabaseClient()
                    .sql("CREATE TABLE IF NOT EXISTS VENDORS (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, n_employees INT, country VARCHAR(255) NOT NULL )")
                    .then();

            Mono<Void> initialData = template.getDatabaseClient()
                    .sql("INSERT INTO VENDORS (name, n_employees, country) VALUES ('Vendor', 100, 'USA'), ('Vendor1', 200, 'Canada')")
                    .then();

            schemaCreation.then(initialData)
                    .then(template
                            .getDatabaseClient()
                            .sql("ALTER TABLE VENDORS ADD CONSTRAINT unique_vendor UNIQUE (name, country)")
                            .then())
                    .subscribe();


        };
    }
}

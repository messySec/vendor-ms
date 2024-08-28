package com.github.Hugornda.vendor_ms.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class TestUtils {

    public static String loadGraphQLQuery(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        String query = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        return String.format( "{ \"query\": \" %s \" }",query)
                .replaceAll("\\s+", " ") // Replace multiple spaces with a single space
                .replaceAll("\\s*\\{\\s*", "{") // Remove spaces around curly braces
                .replaceAll("\\s*}\\s*", "}");

    }

}

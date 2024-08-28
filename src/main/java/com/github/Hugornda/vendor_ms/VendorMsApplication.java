package com.github.Hugornda.vendor_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class, JmxAutoConfiguration.class})
public class VendorMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(VendorMsApplication.class, args);
	}
}

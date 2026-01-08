package com.example.ordermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI platformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order & Logistics Platform API")
                        .description("Production-grade Order Management System with Logistics and Financial Workflows.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Platform Engineering")
                                .email("engineering@example.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi fullApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi ordersApi() {
        return GroupedOpenApi.builder()
                .group("orders")
                .pathsToMatch("/api/v1/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi logisticsApi() {
        return GroupedOpenApi.builder()
                .group("logistics")
                .pathsToMatch("/api/v1/delivery-partners/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi platformApi() {
        return GroupedOpenApi.builder()
                .group("platform")
                .pathsToMatch("/actuator/**")
                .build();
    }
}

package com.github.mangila.producer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Producer")
                        .version("1.0")
                        .description("Produce messages")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .name("Mangila")));
    }
}

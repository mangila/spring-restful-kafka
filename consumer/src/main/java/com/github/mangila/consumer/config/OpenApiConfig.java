package com.github.mangila.consumer.config;

import com.github.mangila.common.Employee;
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
                        .title("Consumer")
                        .version("1.0")
                        .description("Consume messages")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .name("Mangila")));
    }
}

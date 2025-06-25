package com.estapar.parking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Estapar Parking Management API")
                        .version("1.0")
                        .description("API para gestão de estacionamentos, controlando vagas, entrada, saída e faturamento."));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
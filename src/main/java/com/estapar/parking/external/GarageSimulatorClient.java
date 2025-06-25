package com.estapar.parking.external;

import com.estapar.parking.external.dto.GarageConfigResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class GarageSimulatorClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${garage.simulator.url}")
    private String simulatorUrl;

    @Value("${garage.simulator.timeout:30s}")
    private Duration timeout;

    public GarageConfigResponse getGarageConfiguration() {
        log.info("Fetching garage configuration from: {}", simulatorUrl);

        return webClientBuilder
                .baseUrl(simulatorUrl)
                .build()
                .get()
                .uri("/garage")
                .retrieve()
                .bodyToMono(GarageConfigResponse.class)
                .timeout(timeout)
                .doOnSuccess(response -> log.info("Successfully fetched garage configuration"))
                .doOnError(error -> log.error("Failed to fetch garage configuration", error))
                .block();
    }
}
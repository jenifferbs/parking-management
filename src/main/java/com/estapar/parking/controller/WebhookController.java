package com.estapar.parking.controller;

import com.estapar.parking.model.dto.webhook.WebhookEvent;
import com.estapar.parking.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Webhook", description = "Recebimento de eventos do simulador de estacionamento")
public class WebhookController {

    private final VehicleService vehicleService;

    @PostMapping
    @Operation(summary = "Recebe e processa eventos do simulador",
            description = "Este endpoint é invocado pelo simulador para notificar entradas, estacionamento e saídas de veículos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento processado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Tipo de evento desconhecido ou dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao processar o evento")
    })
    public ResponseEntity<Void> handleWebhookEvent(@RequestBody WebhookEvent event) {
        log.info("Received webhook event: {}", event);

        switch (event.eventType()) {
            case ENTRY -> vehicleService.handleVehicleEntry(event);
            case PARKED -> vehicleService.handleVehicleParked(event);
            case EXIT -> vehicleService.handleVehicleExit(event);
            default -> log.warn("Unknown event type: {}", event.eventType());
        }

        return ResponseEntity.ok().build();
    }
}
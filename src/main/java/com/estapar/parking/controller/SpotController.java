package com.estapar.parking.controller;

import com.estapar.parking.model.dto.request.SpotStatusRequest;
import com.estapar.parking.model.dto.response.SpotStatusResponse;
import com.estapar.parking.service.SpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "Vagas", description = "Endpoints para consulta de status de vagas de estacionamento")
public class SpotController {

    private final SpotService spotService;

    @PostMapping("/spot-status")
    @Operation(summary = "Consulta o status de uma vaga específica pelas coordenadas geográficas",
            description = "Retorna informações se a vaga está ocupada e, se sim, dados da sessão de estacionamento atual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da vaga retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: coordenadas ausentes)"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada para as coordenadas fornecidas")
    })
    public ResponseEntity<SpotStatusResponse> getSpotStatus(@Valid @RequestBody SpotStatusRequest request) {
        var response = spotService.getSpotStatus(request.lat(), request.lng());
        return ResponseEntity.ok(response);
    }
}
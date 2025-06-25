package com.estapar.parking.controller;

import com.estapar.parking.model.dto.request.PlateStatusRequest;
import com.estapar.parking.model.dto.response.PlateStatusResponse;
import com.estapar.parking.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "Endpoints para consulta de status de placas de veículos")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/plate-status")
    @Operation(summary = "Consulta o status de estacionamento de um veículo pela placa",
            description = "Retorna o status atual do estacionamento para a placa especificada, incluindo tempo de permanência e preço parcial.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da placa retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: placa ausente)"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado ou não estacionado no momento")
    })
    public ResponseEntity<PlateStatusResponse> getPlateStatus(@RequestBody PlateStatusRequest request) {
        var response = vehicleService.getPlateStatus(request.licensePlate());
        return ResponseEntity.ok(response);
    }
}
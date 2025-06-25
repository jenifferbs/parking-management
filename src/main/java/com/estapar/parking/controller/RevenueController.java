package com.estapar.parking.controller;

import com.estapar.parking.model.dto.request.RevenueRequest;
import com.estapar.parking.model.dto.response.RevenueResponse;
import com.estapar.parking.service.RevenueService;
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
@Tag(name = "Faturamento", description = "Endpoints para consulta de faturamento")
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/revenue")
    @Operation(summary = "Calcula o faturamento total para uma data e opcionalmente um setor",
            description = "Retorna o valor total faturado de todas as sessões de estacionamento concluídas para o dia especificado, podendo ser filtrado por setor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faturamento calculado e retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: formato de data incorreto)")
    })
    public ResponseEntity<RevenueResponse> getRevenue(@Valid @RequestBody RevenueRequest request) {
        var response = revenueService.calculateRevenue(request.date(), request.sector());
        return ResponseEntity.ok(response);
    }
}
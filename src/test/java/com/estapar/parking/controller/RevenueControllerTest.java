package com.estapar.parking.controller;

import com.estapar.parking.model.dto.response.RevenueResponse;
import com.estapar.parking.service.RevenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RevenueController.class)
public class RevenueControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private RevenueService revenueService;

    @Test
    void revenue_ok() throws Exception {
        var response = RevenueResponse.builder()
                .currency("BRL")
                .amount(BigDecimal.valueOf(100))
                .timestamp(LocalDateTime.of(2025, 6, 24, 10, 0, 0))
                .build();

        when(revenueService.calculateRevenue(LocalDate.of(2025, 6, 24), "A")).thenReturn(response);

        mvc.perform(get("/revenue")
                        .contentType("application/json")
                        .content("""
                                {
                                    "date": "2025-06-24",
                                    "sector": "A"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("BRL"))
                .andExpect(jsonPath("$.amount").value(100));
    }
}

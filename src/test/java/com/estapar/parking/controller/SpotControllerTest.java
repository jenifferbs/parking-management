package com.estapar.parking.controller;

import com.estapar.parking.model.dto.response.SpotStatusResponse;
import com.estapar.parking.service.SpotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpotController.class)
class SpotControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private SpotService spotService;

    @Test
    void spotStatus_ok() throws Exception {
        var dto = SpotStatusResponse.builder()
                .occupied(true)
                .build();

        when(spotService.getSpotStatus(new BigDecimal("1.0"), new BigDecimal("2.0")))
                .thenReturn(dto);

        mvc.perform(post("/spot-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         {"lat":1.0,"lng":2.0}
                         """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupied").value(true));

        verify(spotService).getSpotStatus(new BigDecimal("1.0"), new BigDecimal("2.0"));
    }
}

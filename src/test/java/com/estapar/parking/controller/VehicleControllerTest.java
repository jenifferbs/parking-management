package com.estapar.parking.controller;

import com.estapar.parking.model.dto.response.PlateStatusResponse;
import com.estapar.parking.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired private MockMvc mvc;
    @MockitoBean private VehicleService vehicleService;

    @Test
    void plateStatus_returns200_andBody() throws Exception {
        var response = PlateStatusResponse.builder()
                .licensePlate("ABC1234")
                .priceUntilNow(BigDecimal.TEN)
                .build();

        when(vehicleService.getPlateStatus("ABC1234")).thenReturn(response);

        mvc.perform(post("/plate-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                         {"license_plate":"ABC1234"}
                         """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.license_plate").value("ABC1234"))
                .andExpect(jsonPath("$.price_until_now").value(10));

        verify(vehicleService).getPlateStatus("ABC1234");
    }
}


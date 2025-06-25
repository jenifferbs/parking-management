package com.estapar.parking.controller;

import com.estapar.parking.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private VehicleService vehicleService;

    private static final String TEMPLATE = """
        {
          "license_plate":"XYZ9E99",
          "entry_time":"2025-06-24T12:00:00",
          "exit_time":"2025-06-24T13:00:00",
          "lat":10.1,
          "lng":20.2,
          "event_type":"%s"
        }
        """;

    @Test
    void entryEvent_callsHandleVehicleEntry() throws Exception {
        mvc.perform(post("/webhook")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(TEMPLATE.formatted("ENTRY")))
                .andExpect(status().isOk());

        verify(vehicleService).handleVehicleEntry(any());
    }

    @Test
    void parkedEvent_callsHandleVehicleParked() throws Exception {
        mvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TEMPLATE.formatted("PARKED")))
                .andExpect(status().isOk());

        verify(vehicleService).handleVehicleParked(any());
    }

    @Test
    void exitEvent_callsHandleVehicleExit() throws Exception {
        mvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TEMPLATE.formatted("EXIT")))
                .andExpect(status().isOk());

        verify(vehicleService).handleVehicleExit(any());
    }
}

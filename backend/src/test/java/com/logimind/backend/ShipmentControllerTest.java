
package com.logimind.backend;

import com.logimind.backend.controller.ShipmentController;
import com.logimind.backend.entity.Shipment;
import com.logimind.backend.entity.ShipmentStatus;
import com.logimind.backend.service.ShipmentService;
import com.logimind.backend.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ShipmentControllerTest {

    // TEST 1: Shipment exists - HTTP 200
    @Test
    void shouldReturnShipmentWhenIdExists() throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        Shipment shipment = new Shipment();
        shipment.setTrackingNumber("LM1001");
        shipment.setOrigin("Virginia");
        shipment.setDestination("New York");

        when(shipmentService.getShipmentById(2L))
                .thenReturn(Optional.of(shipment));

        mockMvc.perform(get("/api/shipments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber")
                        .value("LM1001"));

        verify(shipmentService).getShipmentById(2L);
    }

    // TEST 2: Shipment does not exist - HTTP 404
    @Test
    void shouldReturn404WhenShipmentDoesNotExist()
            throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        when(shipmentService.getShipmentById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/shipments/999"))
                .andExpect(status().isNotFound());

        verify(shipmentService).getShipmentById(999L);
    }

    // TEST 3: Valid shipment - HTTP 201
    @Test
    void shouldCreateShipmentSuccessfully() throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        Shipment savedShipment = new Shipment();
        savedShipment.setTrackingNumber("LM1005");
        savedShipment.setOrigin("Virginia");
        savedShipment.setDestination("New York");
        savedShipment.setStatus(ShipmentStatus.CREATED);

        when(shipmentService.createShipment(any(Shipment.class)))
                .thenReturn(savedShipment);

        String requestBody = """
                {
                    "trackingNumber": "LM1005",
                    "origin": "Virginia",
                    "destination": "New York",
                    "status": "CREATED"
                }
                """;

        mockMvc.perform(post("/api/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trackingNumber")
                        .value("LM1005"))
                .andExpect(jsonPath("$.origin")
                        .value("Virginia"))
                .andExpect(jsonPath("$.status")
                        .value("CREATED"));

        verify(shipmentService)
                .createShipment(any(Shipment.class));
    }

    // TEST 4: Invalid shipment - HTTP 400
    @Test
    void shouldReturn400ForInvalidShipment() throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        String invalidRequest = """
                {
                    "trackingNumber": "",
                    "origin": "",
                    "destination": "New York"
                }
                """;

        mockMvc.perform(post("/api/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.trackingNumber")
                        .value("Tracking number is required"))
                .andExpect(jsonPath("$.errors.origin")
                        .value("Origin is required"));

        verify(shipmentService, never())
                .createShipment(any(Shipment.class));
    }

    // TEST 5: Get all shipments - HTTP 200
    @Test
    void shouldReturnAllShipments() throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        Shipment shipment = new Shipment();
        shipment.setId(1L);
        shipment.setTrackingNumber("LM1001");
        shipment.setOrigin("Virginia");
        shipment.setDestination("New York");
        shipment.setStatus(ShipmentStatus.CREATED);

        when(shipmentService.getAllShipments())
                .thenReturn(List.of(shipment));

        mockMvc.perform(get("/api/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trackingNumber")
                        .value("LM1001"))
                .andExpect(jsonPath("$[0].status")
                        .value("CREATED"));

        verify(shipmentService).getAllShipments();
    }

    // TEST 6: Valid status update - HTTP 200
    @Test
    void shouldUpdateShipmentStatusSuccessfully() throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        Shipment updatedShipment = new Shipment();
        updatedShipment.setId(1L);
        updatedShipment.setTrackingNumber("LM1001");
        updatedShipment.setOrigin("Virginia");
        updatedShipment.setDestination("New York");
        updatedShipment.setStatus(ShipmentStatus.IN_TRANSIT);

        when(shipmentService.updateShipmentStatus(
                1L, ShipmentStatus.IN_TRANSIT))
                .thenReturn(Optional.of(updatedShipment));

        mockMvc.perform(put("/api/shipments/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"IN_TRANSIT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("IN_TRANSIT"))
                .andExpect(jsonPath("$.trackingNumber")
                        .value("LM1001"));

        verify(shipmentService)
                .updateShipmentStatus(
                        1L, ShipmentStatus.IN_TRANSIT);
    }

    // TEST 7: Invalid status - HTTP 400
    @Test
    void shouldReturn400ForInvalidShipmentStatus()
            throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mockMvc.perform(put("/api/shipments/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"LOST\"}"))
                .andExpect(status().isBadRequest());

        verify(shipmentService, never())
                .updateShipmentStatus(
                        anyLong(), any(ShipmentStatus.class));
    }

    // TEST 8: Nonexistent shipment status update - HTTP 404
    @Test
    void shouldReturn404WhenUpdatingNonexistentShipment()
            throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        when(shipmentService.updateShipmentStatus(
                999L, ShipmentStatus.DELIVERED))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/shipments/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"DELIVERED\"}"))
                .andExpect(status().isNotFound());

        verify(shipmentService)
                .updateShipmentStatus(
                        999L, ShipmentStatus.DELIVERED);
    }

    // TEST 9: Delete existing shipment - HTTP 204
    @Test
    void shouldDeleteShipmentSuccessfully() throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        Shipment shipment = new Shipment();
        shipment.setId(1L);
        shipment.setTrackingNumber("LM1001");
        shipment.setOrigin("Virginia");
        shipment.setDestination("New York");

        when(shipmentService.getShipmentById(1L))
                .thenReturn(Optional.of(shipment));

        mockMvc.perform(delete("/api/shipments/1"))
                .andExpect(status().isNoContent());

        verify(shipmentService).getShipmentById(1L);
        verify(shipmentService).deleteShipment(1L);
    }

    // TEST 10: Delete nonexistent shipment - HTTP 404
    @Test
    void shouldReturn404WhenDeletingNonexistentShipment()
            throws Exception {

        ShipmentService shipmentService = mock(ShipmentService.class);

        ShipmentController controller =
                new ShipmentController(shipmentService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        when(shipmentService.getShipmentById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/shipments/999"))
                .andExpect(status().isNotFound());

        verify(shipmentService).getShipmentById(999L);
        verify(shipmentService, never()).deleteShipment(999L);
    }
}


package com.logimind.backend;

import com.logimind.backend.entity.Shipment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipmentTest {

    @Test
    void shouldSetShipmentStatus() {

        // Arrange: Create a shipment
        Shipment shipment = new Shipment();

        // Act: Set shipment status
        shipment.setStatus("IN_TRANSIT");

        // Assert: Check expected result
        assertEquals("IN_TRANSIT", shipment.getStatus());
    }
}

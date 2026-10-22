
package com.logimind.backend;

import com.logimind.backend.entity.Shipment;
import com.logimind.backend.entity.ShipmentStatus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipmentTest {

    @Test
    void shouldSetShipmentStatus() {

        // Arrange: Create a shipment
        Shipment shipment = new Shipment();

        // Act: Set shipment status using enum
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);

        // Assert: Check expected result
        assertEquals(
                ShipmentStatus.IN_TRANSIT,
                shipment.getStatus()
        );
    }
}

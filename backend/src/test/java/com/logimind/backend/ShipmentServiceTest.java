
package com.logimind.backend;

import com.logimind.backend.entity.Shipment;
import com.logimind.backend.entity.ShipmentStatus;
import com.logimind.backend.repository.ShipmentRepository;
import com.logimind.backend.service.ShipmentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    @Test
    void shouldCreateShipment() {

        // Arrange
        Shipment shipment = new Shipment();
        shipment.setTrackingNumber("LM1002");
        shipment.setOrigin("Virginia");
        shipment.setDestination("New York");

        when(shipmentRepository.save(shipment))
                .thenReturn(shipment);

        // Act
        Shipment result = shipmentService.createShipment(shipment);

        // Assert
        assertEquals("LM1002", result.getTrackingNumber());
        assertEquals("Virginia", result.getOrigin());

        verify(shipmentRepository, times(1)).save(shipment);
    }

    @Test
    void shouldReturnShipmentWhenIdExists() {

        // Arrange
        Shipment shipment = new Shipment();
        shipment.setTrackingNumber("LM1003");

        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.of(shipment));

        // Act
        Optional<Shipment> result =
                shipmentService.getShipmentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(
                "LM1003",
                result.get().getTrackingNumber()
        );

        verify(shipmentRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {

        // Arrange
        when(shipmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Shipment> result =
                shipmentService.getShipmentById(999L);

        // Assert
        assertTrue(result.isEmpty());

        verify(shipmentRepository).findById(999L);
    }

    @Test
    void shouldUpdateShipmentStatusWhenIdExists() {

        // Arrange
        Shipment shipment = new Shipment();
        shipment.setStatus(ShipmentStatus.CREATED);

        when(shipmentRepository.findById(2L))
                .thenReturn(Optional.of(shipment));

        when(shipmentRepository.save(shipment))
                .thenReturn(shipment);

        // Act
        Optional<Shipment> result =
                shipmentService.updateShipmentStatus(
                        2L,
                        ShipmentStatus.IN_TRANSIT
                );

        // Assert
        assertTrue(result.isPresent());
        assertEquals(
                ShipmentStatus.IN_TRANSIT,
                result.get().getStatus()
        );

        verify(shipmentRepository).findById(2L);
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void shouldNotUpdateShipmentWhenIdDoesNotExist() {

        // Arrange
        when(shipmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Shipment> result =
                shipmentService.updateShipmentStatus(
                        999L,
                        ShipmentStatus.IN_TRANSIT
                );

        // Assert
        assertTrue(result.isEmpty());

        verify(shipmentRepository).findById(999L);
        verify(shipmentRepository, never())
                .save(any(Shipment.class));
    }

    @Test
    void shouldDeleteShipment() {

        // Arrange
        Long shipmentId = 2L;

        // Act
        shipmentService.deleteShipment(shipmentId);

        // Assert
        verify(shipmentRepository, times(1))
                .deleteById(shipmentId);
    }

    @Test
    void shouldDeleteShipmentWithDifferentId() {

        // Arrange
        Long shipmentId = 999L;

        // Act
        shipmentService.deleteShipment(shipmentId);

        // Assert
        verify(shipmentRepository, times(1))
                .deleteById(shipmentId);
    }
}

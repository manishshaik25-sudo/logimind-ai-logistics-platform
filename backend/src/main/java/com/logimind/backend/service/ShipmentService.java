
package com.logimind.backend.service;

import com.logimind.backend.entity.Shipment;
import com.logimind.backend.entity.ShipmentStatus;
import com.logimind.backend.repository.ShipmentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    // Create a new shipment
    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    // Retrieve all shipments
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    // Retrieve a shipment by ID
    public Optional<Shipment> getShipmentById(Long id) {
        return shipmentRepository.findById(id);
    }

    // Delete a shipment by ID
    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }

    // Update shipment status using enum
    public Optional<Shipment> updateShipmentStatus(
            Long id,
            ShipmentStatus status) {

        return shipmentRepository.findById(id)
                .map(shipment -> {
                    shipment.setStatus(status);
                    return shipmentRepository.save(shipment);
                });
    }
}


package com.logimind.backend.controller;

import com.logimind.backend.entity.Shipment;
import com.logimind.backend.entity.ShipmentStatus;
import com.logimind.backend.service.ShipmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    // Create a new shipment
    @PostMapping
    public ResponseEntity<Shipment> createShipment(
            @Valid @RequestBody Shipment shipment) {

        Shipment saved = shipmentService.createShipment(shipment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // Retrieve all shipments
    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    // Retrieve shipment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(
            @PathVariable Long id) {

        return shipmentService.getShipmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // Delete shipment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(
            @PathVariable Long id) {

        if (shipmentService.getShipmentById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        shipmentService.deleteShipment(id);

        return ResponseEntity.noContent().build();
    }

    // Update shipment status using enum
    @PutMapping("/{id}/status")
    public ResponseEntity<Shipment> updateShipmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String statusValue = request.get("status");

        if (statusValue == null) {
            return ResponseEntity.badRequest().build();
        }

        ShipmentStatus status;

        try {
            status = ShipmentStatus.valueOf(statusValue);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return shipmentService.updateShipmentStatus(id, status)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }
}

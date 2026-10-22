
package com.logimind.backend.controller;

import com.logimind.backend.entity.Shipment;
import com.logimind.backend.service.ShipmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    // Create shipment
    @PostMapping
public ResponseEntity<Shipment> createShipment(
        @Valid @RequestBody Shipment shipment) {

    Shipment saved = shipmentService.createShipment(shipment);

    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
    // Get all shipments
    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    // Get shipment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(
            @PathVariable Long id) {

        return shipmentService.getShipmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete shipment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(
            @PathVariable Long id) {

        if (shipmentService.getShipmentById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        shipmentService.deleteShipment(id);

        return ResponseEntity.noContent().build();
    }
    

@PutMapping("/{id}/status")
public ResponseEntity<Shipment> updateShipmentStatus(
        @PathVariable Long id,
        @RequestBody Map<String, String> request) {

    String status = request.get("status");

    if (status == null ||
            !List.of("CREATED", "IN_TRANSIT", "DELIVERED", "CANCELLED")
                    .contains(status)) {
        return ResponseEntity.badRequest().build();
    }

    return shipmentService.updateShipmentStatus(id, status)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}

}


package com.logimind.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "SHIPMENTS")
public class Shipment {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "shipment_seq"
    )
    @SequenceGenerator(
        name = "shipment_seq",
        sequenceName = "SHIPMENT_SEQ",
        allocationSize = 1
    )
    private Long id;

    @NotBlank(message = "Tracking number is required")
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String trackingNumber;

    @NotBlank(message = "Origin is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String origin;

    @NotBlank(message = "Destination is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ShipmentStatus status = ShipmentStatus.CREATED;

    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public Shipment() {
    }

    @PrePersist
    public void prePersist() {

        if (status == null) {
            status = ShipmentStatus.CREATED;
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

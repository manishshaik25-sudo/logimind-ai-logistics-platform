
package com.logimind.backend.repository;

import com.logimind.backend.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository
        extends JpaRepository<Shipment, Long> {
}

package com.comcast.incidenttracker.repository;

import com.comcast.incidenttracker.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    // SELECT * FROM services WHERE criticality_tier = ?
    List<Service> findByCriticalityTier(String criticalityTier);

    // SELECT * FROM services WHERE status = ?
    List<Service> findByStatus(String status);
}

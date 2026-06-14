package com.comcast.incidenttracker.repository;

import com.comcast.incidenttracker.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    // SELECT * FROM incidents WHERE severity = ?
    List<Incident> findBySeverity(String severity);

    // SELECT * FROM incidents WHERE status = ?
    List<Incident> findByStatus(String status);

    // SELECT * FROM incidents WHERE service.id = ?
    List<Incident> findByServiceId(Long serviceId);
}

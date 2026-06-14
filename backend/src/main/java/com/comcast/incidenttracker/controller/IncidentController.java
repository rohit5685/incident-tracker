package com.comcast.incidenttracker.controller;

import com.comcast.incidenttracker.dto.IncidentRequest;
import com.comcast.incidenttracker.model.Incident;
import com.comcast.incidenttracker.model.Service;
import com.comcast.incidenttracker.model.User;
import com.comcast.incidenttracker.repository.IncidentRepository;
import com.comcast.incidenttracker.repository.ServiceRepository;
import com.comcast.incidenttracker.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentRepository incidentRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public IncidentController(IncidentRepository incidentRepository,
                               ServiceRepository serviceRepository,
                               UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    // GET /api/incidents                 -> all incidents
    // GET /api/incidents?severity=P1     -> filter by severity
    // GET /api/incidents?status=OPEN     -> filter by status
    @GetMapping
    public List<Incident> getIncidents(@RequestParam(required = false) String severity,
                                         @RequestParam(required = false) String status) {
        if (severity != null) {
            return incidentRepository.findBySeverity(severity);
        }
        if (status != null) {
            return incidentRepository.findByStatus(status);
        }
        return incidentRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Incident> createIncident(@Valid @RequestBody IncidentRequest request,
                                                     Authentication authentication) {
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found: " + request.getServiceId()));

        User reporter = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Incident incident = new Incident();
        incident.setService(service);
        incident.setSeverity(request.getSeverity());
        incident.setDescription(request.getDescription());
        incident.setStatus("OPEN");
        incident.setReportedBy(reporter);
        incident.setCreatedAt(Instant.now());

        return ResponseEntity.ok(incidentRepository.save(incident));
    }

    // PATCH /api/incidents/{id}/resolve -> mark an incident resolved
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Incident> resolveIncident(@PathVariable Long id) {
        return incidentRepository.findById(id)
                .map(incident -> {
                    incident.setStatus("RESOLVED");
                    incident.setResolvedAt(Instant.now());
                    return ResponseEntity.ok(incidentRepository.save(incident));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

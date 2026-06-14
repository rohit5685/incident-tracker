package com.comcast.incidenttracker.controller;

import com.comcast.incidenttracker.model.Service;
import com.comcast.incidenttracker.repository.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;

    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // GET /api/services             -> all services
    // GET /api/services?tier=TIER-0 -> filtered by criticality tier
    // GET /api/services?status=DOWN -> filtered by status
    @GetMapping
    public List<Service> getServices(@RequestParam(required = false) String tier,
                                       @RequestParam(required = false) String status) {
        if (tier != null) {
            return serviceRepository.findByCriticalityTier(tier);
        }
        if (status != null) {
            return serviceRepository.findByStatus(status);
        }
        return serviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getService(@PathVariable Long id) {
        return serviceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Service createService(@Valid @RequestBody Service service) {
        return serviceRepository.save(service);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Service> updateStatus(@PathVariable Long id, @RequestBody StatusUpdate update) {
        return serviceRepository.findById(id)
                .map(service -> {
                    service.setStatus(update.getStatus());
                    return ResponseEntity.ok(serviceRepository.save(service));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Small inner DTO for status-only updates
    public static class StatusUpdate {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

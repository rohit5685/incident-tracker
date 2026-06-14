package com.comcast.incidenttracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g. "Enterprise Search API"

    @Column(name = "owner_team", nullable = false)
    private String ownerTeam; // e.g. "EIT SRE"

    @Column(name = "criticality_tier", nullable = false)
    private String criticalityTier; // "TIER-0", "TIER-1", "TIER-2"

    @Column(nullable = false)
    private String status; // "HEALTHY", "DEGRADED", "DOWN"
}

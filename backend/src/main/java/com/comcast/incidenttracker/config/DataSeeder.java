package com.comcast.incidenttracker.config;

import com.comcast.incidenttracker.model.Service;
import com.comcast.incidenttracker.model.User;
import com.comcast.incidenttracker.repository.ServiceRepository;
import com.comcast.incidenttracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Runs once at application startup. If the database is empty, seeds it with
// sample users and services so the app is immediately usable for demos/testing.
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                       ServiceRepository serviceRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

            User sre = new User();
            sre.setUsername("rohit");
            sre.setPassword(passwordEncoder.encode("rohit123"));
            sre.setRole("ROLE_SRE");
            userRepository.save(sre);
        }

        if (serviceRepository.count() == 0) {
            serviceRepository.save(new Service(null, "Enterprise Search API", "EIT SRE", "TIER-0", "HEALTHY"));
            serviceRepository.save(new Service(null, "Patching Automation Service", "EIT SRE", "TIER-1", "HEALTHY"));
            serviceRepository.save(new Service(null, "CCP Oracle Gateway", "DBA Team", "TIER-0", "DEGRADED"));
            serviceRepository.save(new Service(null, "AMA-GPT Backend", "AI Platform", "TIER-1", "HEALTHY"));
        }
    }
}

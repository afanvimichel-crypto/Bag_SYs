package com.bagsys.users.services;

import com.bagsys.users.entities.Role;
import com.bagsys.users.entities.User;
import com.bagsys.users.enums.RoleCode;
import com.bagsys.users.repositories.RoleRepository;
import com.bagsys.users.repositories.UserRepository;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BootstrapAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapAdminService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public BootstrapAdminService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createInitialAdmin(
            String username, String email, String password, String firstName, String lastName) {
        if (username.isBlank() || email.isBlank() || password.length() < 12) {
            throw new IllegalStateException(
                    "Bootstrap admin requires username, email, and a password of at least 12 characters");
        }

        if (userRepository.existsByUsername(username)) {
            LOGGER.info("Bootstrap admin username already exists; no account was changed.");
            return;
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "Bootstrap admin email is already assigned to another user");
        }

        Role adminRole =
                roleRepository
                        .findByCode(RoleCode.ADMIN)
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                "ADMIN role is missing from the database"));
        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);
        LOGGER.info("Initial administrator account created.");
    }
}

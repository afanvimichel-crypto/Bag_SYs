package com.bagsys.config;

import com.bagsys.users.services.BootstrapAdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.bootstrap-admin.enabled", havingValue = "true")
public class BootstrapAdminConfiguration {

    @Bean
    CommandLineRunner bootstrapAdmin(
            BootstrapAdminService bootstrapAdminService,
            @Value("${BOOTSTRAP_ADMIN_USERNAME:}") String username,
            @Value("${BOOTSTRAP_ADMIN_EMAIL:}") String email,
            @Value("${BOOTSTRAP_ADMIN_PASSWORD:}") String password,
            @Value("${BOOTSTRAP_ADMIN_FIRST_NAME:Administrateur}") String firstName,
            @Value("${BOOTSTRAP_ADMIN_LAST_NAME:Initial}") String lastName) {
        return args ->
                bootstrapAdminService.createInitialAdmin(
                        username,
                        email,
                        password,
                        firstName,
                        lastName);
    }
}

package com.photosystem.config;

import com.photosystem.entity.Role;
import com.photosystem.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository) {
        return args -> {
            // 初始化角色
            if (roleRepository.count() == 0) {
                Role userRole = new Role();
                userRole.setName(Role.RoleName.ROLE_USER);
                roleRepository.save(userRole);

                Role adminRole = new Role();
                adminRole.setName(Role.RoleName.ROLE_ADMIN);
                roleRepository.save(adminRole);
            }
        };
    }
}
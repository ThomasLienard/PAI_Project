package com.example.projet_pai.config;

import com.example.projet_pai.entite.Role;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.RoleRepository;
import com.example.projet_pai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Créer les rôles s'ils n'existent pas
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("CUISINIER");
        createRoleIfNotFound("CLIENT");
        createRoleIfNotFound("SERVEUR");

        // Créer un utilisateur admin s'il n'existe pas
        if (!userRepository.findByEmail("admin@example.com").isPresent()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("adminpassword"));
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
    }

    private void createRoleIfNotFound(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (!role.isPresent()) {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
        }
    }
}
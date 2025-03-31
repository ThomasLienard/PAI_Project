package com.example.projet_pai.config;

import com.example.projet_pai.entite.Role;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.entite.Utilisateur;
import com.example.projet_pai.repository.RoleRepository;
import com.example.projet_pai.repository.TableRepository;
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
    private TableRepository tableRepository;

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
        
        // Initialiser des tables de restaurant
        // Tables pour 2 personnes
        createTableIfNotExist(1, 2);
        createTableIfNotExist(2, 2);
        createTableIfNotExist(3, 2);
        
        // Tables pour 4 personnes
        createTableIfNotExist(4, 4);
        createTableIfNotExist(5, 4);
        createTableIfNotExist(6, 4);
        createTableIfNotExist(7, 4);
        
        // Tables pour 6 personnes
        createTableIfNotExist(8, 6);
        createTableIfNotExist(9, 6);
        
        // Tables pour groupes
        createTableIfNotExist(10, 8);
        createTableIfNotExist(11, 10);
        createTableIfNotExist(12, 12);
    }

    private void createRoleIfNotFound(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (!role.isPresent()) {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
        }
    }
    
    private void createTableIfNotExist(int numero, int capacite) {
        // Vérifier si une table avec ce numéro existe déjà
        if (tableRepository.findAll().stream().noneMatch(t -> t.getNumero() == numero)) {
            Table table = new Table();
            table.setNumero(numero);
            table.setCapacite(capacite);
            tableRepository.save(table);
        }
    }
}
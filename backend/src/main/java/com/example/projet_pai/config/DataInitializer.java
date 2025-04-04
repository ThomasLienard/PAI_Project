package com.example.projet_pai.config;



import com.example.projet_pai.entite.*;
import com.example.projet_pai.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private TagRepository tagRepository;

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
            admin.setPassword(passwordEncoder.encode("Adminpassword1"));
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

        // Créer un utilisateur client par défaut s'il n'existe pas
        if (!userRepository.findByEmail("client@example.com").isPresent()) {
            Role clientRole = roleRepository.findByName("CLIENT").orElseThrow(() -> new RuntimeException("Role not found"));
            Utilisateur client = new Utilisateur();
            client.setUsername("client");
            client.setEmail("client@example.com");
            client.setPassword(passwordEncoder.encode("Clientpassword1"));
            client.setRole(clientRole);
            userRepository.save(client);
        }

        // Créer des catégories
        Category entrees = createCategoryIfNotFound("Entrées");
        Category plats = createCategoryIfNotFound("Plats");
        Category desserts = createCategoryIfNotFound("Desserts");

        // Créer des tags
        Tag vegetarien = createTagIfNotFound("Végétarien", "vegetarian-icon.png");
        Tag vegan = createTagIfNotFound("Végan", "vegan-icon.png");
        Tag sansGluten = createTagIfNotFound("Sans Gluten", "gluten-free-icon.png");

        // Créer des plats
        createDishIfNotFound("Salade César", "Une salade classique avec du poulet, des croûtons et une sauce César.",
                12.5, "http://localhost:8080/images/salade-cesar.png", entrees, Set.of(vegetarien));
        createDishIfNotFound("Steak Frites", "Un steak juteux accompagné de frites croustillantes.",
                18.0, "http://localhost:8080/images/steak-frites.png", plats, Set.of(sansGluten));
        createDishIfNotFound("Tarte aux pommes", "Une tarte maison avec des pommes fraîches.",
                6.0, "http://localhost:8080/images/tarte-aux-pommes.png", desserts, Set.of(vegan, sansGluten));
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


    private Category createCategoryIfNotFound(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(categoryName);
                    return categoryRepository.save(category);
                });
    }

    private Tag createTagIfNotFound(String tagName, String icon) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    tag.setIcon(icon);
                    return tagRepository.save(tag);
                });
    }

    private void createDishIfNotFound(String name, String description, double price, String imageUrl,
                                      Category category, Set<Tag> tags) {
        if (!dishRepository.findByName(name).isPresent()) {
            Dish dish = new Dish();
            dish.setName(name);
            dish.setDescription(description);
            dish.setPrice(price);
            dish.setImageUrl(imageUrl);
            dish.setCategory(category);
            dish.setTags(tags);
            dishRepository.save(dish);
        }
    }
}
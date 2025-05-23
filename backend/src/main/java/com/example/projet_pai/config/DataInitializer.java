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

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository; // Ajouté pour gérer les recettes

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

        // Créer un utilisateur serveur par défaut s'il n'existe pas
        if (!userRepository.findByEmail("serveur@example.com").isPresent()) {
            Role serveurRole = roleRepository.findByName("SERVEUR").orElseThrow(() -> new RuntimeException("Role not found"));
            Utilisateur serveur = new Utilisateur();
            serveur.setUsername("serveur");
            serveur.setEmail("serveur@example.com");
            serveur.setPassword(passwordEncoder.encode("Serveurpassword1"));
            serveur.setRole(serveurRole);
            userRepository.save(serveur);
        }

        // Créer un utilisateur cuisinier par défaut s'il n'existe pas
        if (!userRepository.findByEmail("cuisinier@example.com").isPresent()) {
            Role cuisinierRole = roleRepository.findByName("CUISINIER").orElseThrow(() -> new RuntimeException("Role not found"));
            Utilisateur cuisinier = new Utilisateur();
            cuisinier.setUsername("cuisinier");
            cuisinier.setEmail("cuisinier@example.com");
            cuisinier.setPassword(passwordEncoder.encode("Cuisinierpassword1"));
            cuisinier.setRole(cuisinierRole);
            userRepository.save(cuisinier);
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

        // Créer 2 catégories d'ingrédients si elles n'existent pas
        IngredientCategory legumes = createIngredientCategoryIfNotFound("Légumes", "http://localhost:8080/images/category-icons/legumes.png");
        IngredientCategory viandes = createIngredientCategoryIfNotFound("Viandes", "http://localhost:8080/images/category-icons/viandes.png");

        // Créer 4 ingrédients si ils n'existent pas
        createIngredientIfNotFound("Tomate", "Tomate fraîche", "kg", "http://localhost:8080/images/ingredient-photos/tomate.jpg", legumes, 50.0, 10.0, 20.0, 7);
        createIngredientIfNotFound("Carotte", "Carotte bio", "kg", "http://localhost:8080/images/ingredient-photos/carotte.jpg", legumes, 30.0, 5.0, 10.0, 10);
        createIngredientIfNotFound("Poulet", "Blanc de poulet", "kg", "http://localhost:8080/images/ingredient-photos/poulet.jpg", viandes, 20.0, 5.0, 10.0, 5);   
        
        // --- Création d'une recette "Recette Salade César" ---
        if (!recipeRepository.findByName("Recette Salade César").isPresent()) {
            Recipe recetteCesar = new Recipe("Recette Salade César", "portion", 1);

            // Ajout des ingrédients à la recette
            Ingredient tomate = ingredientRepository.findByName("Tomate").orElseThrow();
            Ingredient poulet = ingredientRepository.findByName("Poulet").orElseThrow();

            RecipeIngredient ri1 = new RecipeIngredient();
            ri1.setRecipe(recetteCesar);
            ri1.setIngredient(tomate);
            ri1.setQuantite(2);
            ri1.setUnite("pièce");

            RecipeIngredient ri2 = new RecipeIngredient();
            ri2.setRecipe(recetteCesar);
            ri2.setIngredient(poulet);
            ri2.setQuantite(0.2);
            ri2.setUnite("kg");

            recetteCesar.getRecipeIngredients().add(ri1);
            recetteCesar.getRecipeIngredients().add(ri2);

            recipeRepository.save(recetteCesar);
        }

        // Lier la recette à un plat existant
        Optional<Recipe> recetteCesarOpt = recipeRepository.findByName("Recette Salade César");
        Optional<Dish> saladeCesarOpt = dishRepository.findByName("Salade César");
        if (recetteCesarOpt.isPresent() && saladeCesarOpt.isPresent()) {
            Dish saladeCesar = saladeCesarOpt.get();
            saladeCesar.setRecipe(recetteCesarOpt.get());
            dishRepository.save(saladeCesar);
        }
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

    private IngredientCategory createIngredientCategoryIfNotFound(String name, String iconPath) {
        return ingredientCategoryRepository.findByName(name)
                .orElseGet(() -> {
                    IngredientCategory cat = new IngredientCategory();
                    cat.setName(name);
                    cat.setIcon(iconPath);
                    return ingredientCategoryRepository.save(cat);
                });
    }

    private void createIngredientIfNotFound(String name, String description, String unit, String photoUrl,
                                        IngredientCategory category, Double initialStock, Double alertThreshold,
                                        Double recommendedOrderQty, Integer shelfLifeDays) {
        if (!ingredientRepository.findByName(name).isPresent()) {
            Ingredient ing = new Ingredient();
            ing.setName(name);
            ing.setDescription(description);
            ing.setUnit(unit);
            ing.setPhotoUrl(photoUrl);
            ing.setCategory(category);
            ing.setStock(initialStock);
            ing.setAlertThreshold(alertThreshold);
            ing.setRecommendedOrderQty(recommendedOrderQty);
            ing.setShelfLifeDays(shelfLifeDays);
            ingredientRepository.save(ing);
        }
    }
}
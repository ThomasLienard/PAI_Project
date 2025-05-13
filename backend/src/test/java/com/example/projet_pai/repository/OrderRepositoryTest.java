package com.example.projet_pai.repository;

import com.example.projet_pai.entite.Order;
import com.example.projet_pai.entite.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableRepository tableRepository;

    private Table table1;
    private Table table2;

    @BeforeEach
    void setUp() {
        // Créer des tables
        table1 = new Table();
        table1.setNumero(1);
        table1.setCapacite(4);
        entityManager.persist(table1);

        table2 = new Table();
        table2.setNumero(2);
        table2.setCapacite(6);
        entityManager.persist(table2);

        // Créer des commandes
        Order order1 = new Order(table1, "Client 1");
        order1.setStatus("en_attente");
        entityManager.persist(order1);

        Order order2 = new Order(table1, "Client 2");
        order2.setStatus("en_preparation");
        entityManager.persist(order2);

        Order order3 = new Order(table2, "Client 3");
        order3.setStatus("pret");
        entityManager.persist(order3);

        Order order4 = new Order(table2, "Client 4");
        order4.setStatus("servi");
        entityManager.persist(order4);

        entityManager.flush();
    }

    @Test
    void testFindCurrentOrders() {
        // Exécution du test
        List<Order> currentOrders = orderRepository.findCurrentOrders();

        // Vérification des résultats
        assertEquals(3, currentOrders.size());
        assertTrue(currentOrders.stream().noneMatch(order -> "servi".equals(order.getStatus())));
    }

    @Test
    void testFindCurrentOrdersByTable() {
        // Exécution du test
        List<Order> currentOrdersTable1 = orderRepository.findCurrentOrdersByTable(table1);
        List<Order> currentOrdersTable2 = orderRepository.findCurrentOrdersByTable(table2);

        // Vérification des résultats
        assertEquals(2, currentOrdersTable1.size());
        assertEquals(1, currentOrdersTable2.size());
        
        assertTrue(currentOrdersTable1.stream().allMatch(order -> 
            "en_attente".equals(order.getStatus()) || "en_preparation".equals(order.getStatus())));
        
        assertTrue(currentOrdersTable2.stream().allMatch(order -> 
            "pret".equals(order.getStatus())));
    }
}
package com.example.projet_pai.service.Impl;

import com.example.projet_pai.dto.OrderDTO;
import com.example.projet_pai.dto.OrderItemRequest;
import com.example.projet_pai.dto.OrderRequest;
import com.example.projet_pai.entite.Dish;
import com.example.projet_pai.entite.Order;
import com.example.projet_pai.entite.OrderItem;
import com.example.projet_pai.entite.Table;
import com.example.projet_pai.repository.DishRepository;
import com.example.projet_pai.repository.OrderItemRepository;
import com.example.projet_pai.repository.OrderRepository;
import com.example.projet_pai.repository.TableRepository;
import com.example.projet_pai.service.ServerOrderServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerOrderServiceImpl implements ServerOrderServiceItf {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private TableRepository tableRepository;
    
    @Autowired
    private DishRepository dishRepository;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequest orderRequest) {
        // Validation des données
        if (orderRequest.getTableId() == null || orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("La commande doit avoir une table et au moins un article");
        }
        
        // Récupération de la table
        Table table = tableRepository.findById(orderRequest.getTableId())
                .orElseThrow(() -> new RuntimeException("Table non trouvée: " + orderRequest.getTableId()));
        
        // Création de la commande
        Order order = new Order(table, orderRequest.getClientName());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("en_attente");
        order.setAdditional(orderRequest.isAdditional());
        
        // Ajout des articles
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Dish dish = dishRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new RuntimeException("Plat non trouvé: " + itemRequest.getItemId()));
            
            OrderItem orderItem = new OrderItem(dish, itemRequest.getQuantity(), itemRequest.getSpecialInstructions());
            order.addItem(orderItem);
        }
        
        // Sauvegarde de la commande
        Order savedOrder = orderRepository.save(order);
        
        return new OrderDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> getCurrentOrders() {
        List<Order> orders = orderRepository.findCurrentOrders();
        return OrderDTO.fromOrderList(orders);
    }

    @Override
    public List<OrderDTO> getCurrentOrdersByTable(Long tableId) {
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table non trouvée: " + tableId));
        
        List<Order> orders = orderRepository.findCurrentOrdersByTable(table);
        return OrderDTO.fromOrderList(orders);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        
        // Validation du statut
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Statut invalide: " + status);
        }
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        return new OrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée: " + orderId));
        
        // On ne peut annuler que les commandes en attente ou en préparation
        if (!order.getStatus().equals("en_attente") && !order.getStatus().equals("en_preparation")) {
            throw new IllegalStateException("Impossible d'annuler une commande qui est déjà " + order.getStatus());
        }
        
        orderRepository.delete(order);
    }
    
    private boolean isValidStatus(String status) {
        return status != null && (
                status.equals("en_attente") || 
                status.equals("en_preparation") || 
                status.equals("pret") || 
                status.equals("servi"));
    }
}
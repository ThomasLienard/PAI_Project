package com.example.projet_pai.service.Impl;

import com.example.projet_pai.entite.Supplier;
import com.example.projet_pai.entite.SupplierOrder;
import com.example.projet_pai.entite.SupplierProduct;
import com.example.projet_pai.repository.SupplierRepository;
import com.example.projet_pai.repository.SupplierProductRepository;
import com.example.projet_pai.repository.SupplierOrderRepository;
import com.example.projet_pai.service.SupplierServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierServiceItf {
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private SupplierProductRepository productRepository;
    
    @Autowired
    private SupplierOrderRepository orderRepository;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        validateSupplier(supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existingSupplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
        
        validateSupplier(supplier);
        supplier.setId(id);
        return supplierRepository.save(supplier);
    }

    @Override
    public void toggleSupplierStatus(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
        supplier.setActive(!supplier.isActive());
        supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public List<Supplier> searchSuppliers(String query, String filterBy) {
        switch (filterBy) {
            case "name":
                return supplierRepository.findByNameContainingIgnoreCase(query);
            case "location":
                return supplierRepository.findByLocationContainingIgnoreCase(query);
            case "productType":
                return supplierRepository.findByProductTypesContainingIgnoreCase(query);
            default:
                throw new RuntimeException("Critère de filtrage invalide");
        }
    }

    @Override
    public List<Supplier> sortSuppliers(String sortBy) {
        switch (sortBy) {
            case "reliability":
                return supplierRepository.findAllByOrderByRatingDesc();
            case "deliveryTime":
                return supplierRepository.findAllByOrderByAverageDeliveryTimeAsc();
            default:
                return supplierRepository.findAll();
        }
    }

    @Override
    public SupplierProduct addProduct(Long supplierId, SupplierProduct product) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
        product.setSupplier(supplier);
        return productRepository.save(product);
    }

    @Override
    public void toggleProductPreferred(Long productId) {
        SupplierProduct product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        product.setPreferred(!product.isPreferred());
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long productId, SupplierProduct product) {
        SupplierProduct existingProduct = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        product.setId(productId);
        product.setSupplier(existingProduct.getSupplier());
        productRepository.save(product);
    }

    @Override
    public List<SupplierProduct> getSupplierProducts(Long supplierId) {
        return productRepository.findBySupplierId(supplierId);
    }

    // Méthodes privées utilitaires
    private void validateSupplier(Supplier supplier) {
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new RuntimeException("Le nom du fournisseur est obligatoire");
        }
        if (supplier.getEmail() == null || supplier.getEmail().trim().isEmpty()) {
            throw new RuntimeException("L'email du fournisseur est obligatoire");
        }
        // Autres validations...
    }

    @Override
    public List<SupplierProduct> searchProducts(String query, String category) {
        if ((query == null || query.isBlank()) && (category == null || category.isBlank())) {
            return productRepository.findAll();
        }
        if (query != null && !query.isBlank() && (category == null || category.isBlank())) {
            return productRepository.searchProducts(query);
        }
        if ((query == null || query.isBlank()) && category != null && !category.isBlank()) {
            return productRepository.findByCategory(category);
        }
        // Les deux sont renseignés
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category)
                          && (p.getName().toLowerCase().contains(query.toLowerCase())
                              || p.getReference().toLowerCase().contains(query.toLowerCase())))
                .toList();
    }

    @Override
    public SupplierOrder createOrder(Long supplierId, SupplierOrder order) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
        order.setSupplier(supplier);
        order.setDate(java.time.LocalDateTime.now());
        // Calcul du total
        double total = 0.0;
        if (order.getItems() != null) {
            for (var item : order.getItems()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        order.setTotal(total);
        order.setStatus(SupplierOrder.OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        SupplierOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        try {
            SupplierOrder.OrderStatus newStatus = SupplierOrder.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut de commande invalide");
        }
    }

    @Override
    public List<SupplierOrder> getSupplierOrders(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId);
        }
}
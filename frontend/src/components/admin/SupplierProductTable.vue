<template>
  <div class="supplier-product-table">
    <h3>Produits du fournisseur</h3>
    <table v-if="products.length > 0">
      <thead>
        <tr>
          <th>Produit</th>
          <th>Prix Unitaire</th>
          <th>Quantité</th>
          <th>Sous-total</th>
        </tr>
      </thead>
      <tbody>
        <SupplierProductRow
          v-for="product in products"
          :key="product.id"
          :product="product"
          :quantity="localOrderLines[product.id] || 0"
          @update-quantity="updateQuantity(product.id, $event)"
        />
      </tbody>
    </table>
    <p v-else>Ce fournisseur n'a pas de produits associés ou les produits n'ont pas pu être chargés.</p>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';
import SupplierProductRow from './SupplierProductRow.vue';

const props = defineProps({
  products: {
    type: Array,
    required: true,
    default: () => []
  },
  modelValue: { // Pour v-model sur orderLines { productId: quantity }
    type: Object,
    required: true,
    default: () => ({})
  }
});

const emit = defineEmits(['update:modelValue']);

// Copie locale pour éviter la mutation directe de la prop
const localOrderLines = ref({ ...props.modelValue });

// Surveiller les changements de la prop modelValue pour mettre à jour la copie locale
watch(() => props.modelValue, (newValue) => {
  localOrderLines.value = { ...newValue };
}, { deep: true });

const updateQuantity = (productId, quantity) => {
  const newOrderLines = { ...localOrderLines.value };
  if (quantity > 0) {
    newOrderLines[productId] = quantity;
  } else {
    delete newOrderLines[productId]; // Supprimer si la quantité est 0 ou moins
  }
  localOrderLines.value = newOrderLines;
  emit('update:modelValue', newOrderLines);
};
</script>

<style scoped>
.supplier-product-table {
  margin-bottom: 20px;
}
.supplier-product-table table {
  width: 100%;
  border-collapse: collapse;
}
.supplier-product-table th, .supplier-product-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
.supplier-product-table th {
  background-color: #f2f2f2;
}
</style>
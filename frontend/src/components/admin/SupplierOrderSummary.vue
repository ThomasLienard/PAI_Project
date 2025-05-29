<template>
  <div class="order-summary">
    <h3>Récapitulatif</h3>
    <div v-if="totalQuantity > 0">
      <p>Nombre total d'articles : {{ totalQuantity }}</p>
      <p>Sous-total Produits : {{ formatCurrency(productsSubtotal) }}</p>
      <p>Frais de livraison : {{ formatCurrency(supplierFee) }}</p>
      <p><strong>Montant Total : {{ formatCurrency(grandTotal) }}</strong></p>
      <button @click="$emit('submit-order')" :disabled="!canSubmit">
        Envoyer la Commande
      </button>
    </div>
    <p v-else>Aucun article sélectionné.</p>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  lines: Object,
  products: Array,
  canSubmit: Boolean,
  supplierFee: {
    type: Number,
    default: 0
  }
});

const totalQuantity = computed(() =>
  Object.values(props.lines).reduce((sum, qty) => sum + (qty || 0), 0)
);

const productsSubtotal = computed(() =>
  Object.entries(props.lines).reduce((sum, [productId, quantity]) => {
    const product = props.products.find(p => p.id.toString() === productId);
    const price = product ? (product.unitPrice || 0) : 0;
    return sum + (price * (quantity || 0));
  }, 0)
);

const grandTotal = computed(() => productsSubtotal.value + props.supplierFee);

const formatCurrency = value =>
  typeof value === 'number' ? value.toFixed(2) + ' €' : '0.00 €';
</script>

<style scoped>
.order-summary {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  background-color: #f9f9f9;
}
.order-summary button {
  padding: 10px 15px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}
.order-summary button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
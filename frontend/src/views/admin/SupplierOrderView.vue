<template>
  <div class="supplier-order-view">
    <NavBar />
    <h1>Nouvelle Commande Fournisseur</h1>

    <SupplierSelector
      :suppliers="suppliers"
      v-model="selectedSupplierId"
      @update:modelValue="handleSupplierChange"
    />

    <SupplierProductTable
      v-if="selectedSupplierId && supplierProducts.length"
      :products="supplierProducts"
      v-model="orderLines"
    />
    <div v-else-if="selectedSupplierId && !loadingProducts">
      <p>Aucun produit trouvé pour ce fournisseur ou le chargement a échoué.</p>
    </div>
    <div v-if="loadingProducts">
      <p>Chargement des produits...</p>
    </div>

    <SupplierOrderSummary
      :lines="orderLines"
      :products="supplierProducts"
      :can-submit="canSubmitOrder"
      :supplier-fee="selectedSupplierFee"
      @submit-order="submitOrder"
    />

    <div v-if="submissionStatus" :class="['submission-status', submissionStatus.type]">
      {{ submissionStatus.message }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import apiClient from '@/services/apiClient';

import SupplierSelector from '@/components/admin/SupplierSelector.vue';
import SupplierProductTable from '@/components/admin/SupplierProductTable.vue';
import SupplierOrderSummary from '@/components/admin/SupplierOrderSummary.vue';
import NavBar from '@/components/NavBar.vue';

const suppliers = ref([]);
const selectedSupplierId = ref(null);
const supplierProducts = ref([]);
const orderLines = ref({});
const loadingProducts = ref(false);
const submissionStatus = ref(null);

const selectedSupplierFee = computed(() => {
  if (selectedSupplierId.value) {
    const supplier = suppliers.value.find(s => s.id === selectedSupplierId.value);
    return supplier ? (parseFloat(supplier.deliveryFee) || 0) : 0;
  }
  return 0;
});

onMounted(async () => {
  try {
    const response = await apiClient.get('/admin/suppliers/all');
    suppliers.value = response.data;
  } catch (error) {
    submissionStatus.value = { type: 'error', message: 'Impossible de charger les fournisseurs.' };
  }
});

// Charger les produits quand un fournisseur est sélectionné
const fetchSupplierProducts = async (supplierId) => {
  if (!supplierId) {
    supplierProducts.value = [];
    orderLines.value = {};
    return;
  }
  loadingProducts.value = true;
  submissionStatus.value = null;
  try {
    const response = await apiClient.get(`/admin/suppliers/${supplierId}/products`);
    supplierProducts.value = response.data.map(p => ({ 
      ...p, 
      unitPrice: parseFloat(p.price) || 0
    })); 
    orderLines.value = {};
  } catch (error) {
    supplierProducts.value = [];
    submissionStatus.value = { type: 'error', message: 'Impossible de charger les produits du fournisseur.' };
  } finally {
    loadingProducts.value = false;
  }
};

const handleSupplierChange = (newSupplierId) => {
  selectedSupplierId.value = newSupplierId;
  fetchSupplierProducts(newSupplierId);
};

// Calculer si la commande peut être soumise
const canSubmitOrder = computed(() => {
  return selectedSupplierId.value &&
         supplierProducts.value.length > 0 &&
         Object.values(orderLines.value).some(qty => qty > 0);
});

// Soumettre la commande
const submitOrder = async () => {
  if (!canSubmitOrder.value) return;

  const linesToSubmit = Object.entries(orderLines.value)
    .filter(([productId, quantity]) => quantity > 0)
    .map(([productId, quantity]) => {
      const product = supplierProducts.value.find(p => p.id.toString() === productId);
      return {
        productId: parseInt(productId),
        quantity: quantity,
        unitPrice: product ? product.unitPrice : 0 // S'assurer d'avoir le prix
      };
    });

  if (linesToSubmit.length === 0) {
    submissionStatus.value = { type: 'error', message: 'Aucun produit sélectionné avec une quantité valide.' };
    return;
  }

  const orderPayload = {
    supplierId: selectedSupplierId.value,
    lines: linesToSubmit,
  };

  submissionStatus.value = null;
  try {
    await apiClient.post('/admin/supplier/orders', orderPayload); // Adapte l'URL
    submissionStatus.value = { type: 'success', message: 'Commande envoyée avec succès !' };
    // Réinitialiser le formulaire ou rediriger
    selectedSupplierId.value = null;
    supplierProducts.value = [];
    orderLines.value = {};
  } catch (error) {
    submissionStatus.value = { type: 'error', message: error.response?.data?.message || 'Erreur lors de l\'envoi de la commande.' };
  }
};
</script>

<style scoped>
.supplier-order-view {
  padding: 20px;
  max-width: 900px;
  margin: auto;
}
.submission-status {
  margin-top: 15px;
  padding: 10px;
  border-radius: 4px;
}
.submission-status.success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}
.submission-status.error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
</style>
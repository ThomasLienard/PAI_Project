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

    <div v-if="showEditOrderModal" class="modal">
      <div class="modal-content">
        <h2>Modifier la commande</h2>
        <button class="btn-secondary" @click="cancelEditOrder">Annuler</button>
        <form @submit.prevent="saveEditedOrder">
          <table>
            <thead>
              <tr>
                <th>Produit</th>
                <th>Quantité reçue</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(line, idx) in editingOrderLines" :key="line.id">
                <td>{{ getProductName(line.productId) }}</td>
                <td>
                  <input type="number" v-model.number="line.quantity" min="0" />
                </td>
                <td>
                  <button
                    v-if="!line.delivered"
                    type="button"
                    class="btn-warning"
                    @click="removeOrderLine(idx)"
                  >
                    Supprimer
                  </button>
                  <span v-else style="color: #aaa;">Livré</span>
                </td>
              </tr>
            </tbody>
          </table>
          <div style="margin-top: 1em;">
            <button type="submit" class="btn-primary">Valider la réception</button>
            <button type="button" class="btn-secondary" @click="cancelEditOrder">Revenir en arrière</button>
          </div>
        </form>
      </div>
    </div>

    <div v-if="orders.length">
      <h2>Commandes en attente</h2>
      <div v-for="order in orders" :key="order.id" class="order-summary">
        <h3>Commande #{{ order.id }}</h3>
        <ul>
          <li v-for="line in order.lines" :key="line.id">
            <strong>{{ getProductName(line.productId) }}</strong>
            — Quantité : {{ line.quantity }}
            — Prix unitaire : {{ line.unitPrice }} €
          </li>
        </ul>
        <button class="btn-primary" @click="modifyOrder(order.id)">Modifier</button>
        <button class="btn-primary" @click="validateOrder(order.id)">Valider</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
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
const showEditOrderModal = ref(false);
const editingOrder = ref(null); 
const editingOrderLines = ref([]); 
const orders = ref([]);

const selectedSupplierFee = computed(() => {
  if (selectedSupplierId.value) {
    const supplier = suppliers.value.find(s => s.id === selectedSupplierId.value);
    return supplier ? (parseFloat(supplier.deliveryFee) || 0) : 0;
  }
  return 0;
});

let refreshInterval = null;

onMounted(async () => {
  try {
    const response = await apiClient.get('/admin/suppliers/all');
    suppliers.value = response.data;
  } catch (error) {
    submissionStatus.value = { type: 'error', message: 'Impossible de charger les fournisseurs.' };
  }
  await fetchPendingOrders();

  // Rafraîchissement automatique toutes les 30 secondes
  refreshInterval = setInterval(fetchPendingOrders, 1000);
});

onUnmounted(() => {
  if (refreshInterval) clearInterval(refreshInterval);
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
    orderLines.value = {};
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

// Gérer l'édition de commande
const modifyOrder = (orderId) => {
  const order = orders.value.find(o => o.id === orderId);
  if (!order) return;
  editingOrder.value = { ...order };
  editingOrderLines.value = order.lines.map(line => ({ ...line }));
  showEditOrderModal.value = true;
};

const cancelEditOrder = () => {
  showEditOrderModal.value = false;
  editingOrder.value = null;
  editingOrderLines.value = [];
};

const removeOrderLine = (idx) => {
  editingOrderLines.value.splice(idx, 1);
};

const saveEditedOrder = async () => {
  try {
    await apiClient.put(`/admin/supplier/orders/${editingOrder.value.id}/update-lines`, editingOrderLines.value);
    await fetchPendingOrders();
    showEditOrderModal.value = false;
    editingOrder.value = null;
    editingOrderLines.value = [];
  } catch (error) {
    submissionStatus.value = { type: 'error', message: "Erreur lors de la modification de la commande" };
    console.error(error);
  }
};

const validateOrder = async (orderId) => {
  try {
    await apiClient.put(`/admin/supplier/orders/${orderId}/validate`);
    await fetchPendingOrders();
  } catch (error) {
    submissionStatus.value = { type: 'error', message: "Erreur lors de la validation de la commande" };
  }
};

const fetchPendingOrders = async () => {
  try {
    const response = await apiClient.get('/admin/supplier/orders/pending');
    orders.value = response.data;
  } catch (error) {
    submissionStatus.value = { type: 'error', message: "Impossible de charger les commandes en attente." };
  }
};

function getProductName(productId) {
  const product = supplierProducts.value.find(p => p.id === productId);
  return product ? product.name : `Produit #${productId}`;
}
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
.modal {
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0 0 0 / 50%);
}
.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
  max-width: 600px;
}
.order-summary {
  margin-top: 2em;
  padding: 1em;
  border: 1px solid #ddd;
  border-radius: 4px;
}
</style>
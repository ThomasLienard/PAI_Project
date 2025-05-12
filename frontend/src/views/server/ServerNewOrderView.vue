<template>
  <NavBar />
  <div class="server-order-view">
    <div class="order-header">
      <div class="table-info" v-if="selectedTable">
        <h2>
          Table #{{ selectedTable.numero }}
          <span class="order-type-badge" :class="{ 'additional-order': isAdditionalOrder }">
            {{ isAdditionalOrder ? 'Commande supplémentaire' : 'Nouvelle commande' }}
          </span>
        </h2>
        <button class="change-table-btn" @click="showTableSelection = true">
          <i class="fas fa-exchange-alt"></i> Changer
        </button>
      </div>
      <div class="action-buttons">
        <button class="cancel-btn" @click="confirmCancel" :disabled="!hasItems">
          <i class="fas fa-times"></i> Annuler
        </button>
        <button class="validate-btn" @click="confirmOrder" :disabled="!hasItems">
          <i class="fas fa-check"></i> Valider la commande
        </button>
      </div>
    </div>

    <!-- Sélection initiale de la table -->
    <TableSelection 
      v-if="!selectedTable || showTableSelection"
      @select-table="selectTable" 
      @close="showTableSelection = false"
    />

    <!-- Corps principal avec menu groupé et commande en cours -->
    <div class="order-content" v-if="selectedTable && !showTableSelection">
      <div class="menu-section">
        <div v-for="(items, categoryName) in menuGrouped" :key="categoryName" class="menu-category">
          <h3>{{ categoryName }}</h3>
          <div class="menu-items">
            <div
              v-for="item in items"
              :key="item.id"
              class="menu-item"
              @click="addItemToOrder(item)"
              style="cursor: pointer; margin-bottom: 8px; padding: 8px; border: 1px solid #eee; border-radius: 4px;"
            >
              <img
                v-if="item.imageUrl"
                :src="item.imageUrl"
                alt="photo du plat"
                style="width: 60px; height: 60px; object-fit: cover; margin-right: 12px; border-radius: 6px;"
              />
              <span>{{ item.name }}</span>
              <span style="float:right;">{{ item.price }} €</span>
            </div>
          </div>
        </div>
      </div>

      <div class="order-section">
        <OrderSummary 
          :orderItems="currentOrder.items"
          :totalAmount="orderTotal"
          @increase-quantity="increaseItemQuantity"
          @decrease-quantity="decreaseItemQuantity"
          @remove-item="removeItemFromOrder"
          @add-instructions="showInstructionsModal"
        />
      </div>
    </div>

    <!-- Modal pour instructions spéciales -->
    <SpecialInstructionsModal
      v-if="showInstructions"
      :item="selectedItemForInstructions"
      @save="saveInstructions"
      @close="showInstructions = false"
    />

    <!-- Dialog de confirmation -->
    <ConfirmationDialog
      v-if="showConfirmation"
      :title="confirmationTitle"
      :message="confirmationMessage"
      :type="confirmationAction === 'cancel' ? 'warning' : 'confirm'"
      :confirmText="confirmationAction === 'cancel' ? 'Abandonner' : 'Envoyer'"
      @confirm="handleConfirmation"
      @cancel="showConfirmation = false"
    />
    
    <!-- Notification de succès -->
    <div class="success-notification" v-if="showSuccessNotification">
      <i class="fas fa-check-circle"></i>
      Commande envoyée avec succès !
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import NavBar from '@/components/NavBar.vue';
import TableSelection from '@/components/server/TableSelection.vue';
import OrderSummary from '@/components/server/OrderSummary.vue';
import SpecialInstructionsModal from '@/components/server/SpecialInstructionsModal.vue';
import ConfirmationDialog from '@/components/server/ConfirmationDialog.vue';
import { getMenuGroupedByCategory } from '@/services/apiClient';
import apiClient from '@/services/apiClient';

export default {
  name: 'ServerNewOrderView',
  components: {
    NavBar,
    TableSelection,
    OrderSummary,
    SpecialInstructionsModal,
    ConfirmationDialog
  },
  setup() {
    const router = useRouter();
    const selectedTable = ref(null);
    const showTableSelection = ref(false);
    const menuGrouped = ref([]);
    const currentOrder = ref({ items: [] });
    const unavailableItems = ref([]);
    const showInstructions = ref(false);
    const selectedItemForInstructions = ref(null);
    const showConfirmation = ref(false);
    const confirmationTitle = ref('');
    const confirmationMessage = ref('');
    const confirmationAction = ref(null);
    const isAdditionalOrder = ref(false);
    const showSuccessNotification = ref(false);

    // Computed properties
    const hasItems = computed(() => currentOrder.value.items.length > 0);

    const orderTotal = computed(() => {
      return currentOrder.value.items.reduce((total, item) => {
        return total + (item.price * item.quantity);
      }, 0);
    });

    // Methods
    const selectTable = (table) => {
      selectedTable.value = table;
      showTableSelection.value = false;
      checkExistingOrders(table.id);
    };

    const checkExistingOrders = async (tableId) => {
      try {
        const response = await apiClient.get(`/server/orders/tables/${tableId}`);
        isAdditionalOrder.value = response.data.length > 0;
      } catch (error) {
        console.error('Erreur lors de la vérification des commandes existantes:', error);
      }
    };

    const loadMenu = async () => {
      try {
        menuGrouped.value = await getMenuGroupedByCategory();
        console.log('menuGrouped:', menuGrouped.value);
      } catch (error) {
        console.error('Erreur lors du chargement du menu groupé:', error);
      }
    };

    const addItemToOrder = (item) => {
      const existingItem = currentOrder.value.items.find(i => i.id === item.id);
      if (existingItem) {
        existingItem.quantity += 1;
      } else {
        currentOrder.value.items.push({
          ...item,
          quantity: 1,
          specialInstructions: ''
        });
      }
    };

    const increaseItemQuantity = (itemId) => {
      const item = currentOrder.value.items.find(i => i.id === itemId);
      if (item) item.quantity += 1;
    };

    const decreaseItemQuantity = (itemId) => {
      const item = currentOrder.value.items.find(i => i.id === itemId);
      if (item && item.quantity > 1) {
        item.quantity -= 1;
      } else if (item) {
        removeItemFromOrder(itemId);
      }
    };

    const removeItemFromOrder = (itemId) => {
      currentOrder.value.items = currentOrder.value.items.filter(i => i.id !== itemId);
    };

    const showInstructionsModal = (item) => {
      selectedItemForInstructions.value = item;
      showInstructions.value = true;
    };

    const saveInstructions = (itemId, instructions) => {
      const item = currentOrder.value.items.find(i => i.id === itemId);
      if (item) {
        item.specialInstructions = instructions;
      }
      showInstructions.value = false;
    };

    const confirmCancel = () => {
      confirmationTitle.value = "Annuler la commande ?";
      confirmationMessage.value = "Êtes-vous sûr de vouloir abandonner cette commande ? Toutes les modifications seront perdues.";
      confirmationAction.value = 'cancel';
      showConfirmation.value = true;
    };

    const confirmOrder = () => {
      confirmationTitle.value = "Confirmer la commande ?";
      confirmationMessage.value = "Êtes-vous sûr de vouloir envoyer cette commande à la cuisine ?";
      confirmationAction.value = 'confirm';
      showConfirmation.value = true;
    };

    const handleConfirmation = async () => {
      if (confirmationAction.value === 'cancel') {
        resetOrder();
        router.push('/server');
      } else if (confirmationAction.value === 'confirm') {
        await submitOrderToKitchen();
      }
      showConfirmation.value = false;
    };

    const submitOrderToKitchen = async () => {
      try {
        const orderData = {
          tableId: selectedTable.value.id,
          items: currentOrder.value.items.map(item => ({
            itemId: item.id,
            quantity: item.quantity,
            specialInstructions: item.specialInstructions
          })),
          isAdditional: isAdditionalOrder.value,
        };
        await apiClient.post('/server/orders', orderData);
        showSuccessNotification.value = true;
        setTimeout(() => {
          showSuccessNotification.value = false;
          resetOrder();
          router.push('/server');
        }, 2000);
      } catch (err) {
        console.error('Erreur lors de l\'envoi de la commande:', err);
      }
    };

    const resetOrder = () => {
      currentOrder.value = { items: [] };
      selectedTable.value = null;
      isAdditionalOrder.value = false;
    };

    onMounted(() => {
      loadMenu();
    });

    return {
      selectedTable,
      showTableSelection,
      menuGrouped,
      currentOrder,
      unavailableItems,
      showInstructions,
      selectedItemForInstructions,
      showConfirmation,
      confirmationTitle,
      confirmationMessage,
      confirmationAction,
      isAdditionalOrder,
      hasItems,
      orderTotal,
      showSuccessNotification,
      selectTable,
      addItemToOrder,
      increaseItemQuantity,
      decreaseItemQuantity,
      removeItemFromOrder,
      showInstructionsModal,
      saveInstructions,
      confirmCancel,
      confirmOrder,
      handleConfirmation
    };
  }
}
</script>

<style scoped>
.server-order-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
  margin-top: 48px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.table-info h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #343a40;
}

.order-type-badge {
  margin-left: 8px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.875rem;
  color: #fff;
}

.additional-order {
  background-color: #ffc107;
}

.change-table-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 1px solid #6c757d;
  border-radius: 4px;
  background-color: #fff;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.3s;
}

.change-table-btn:hover {
  background-color: #6c757d;
  color: #fff;
}

.action-buttons {
  display: flex;
  gap: 16px;
}

.cancel-btn,
.validate-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn {
  background-color: #dc3545;
  color: #fff;
}

.cancel-btn:disabled {
  background-color: #e9ecef;
  color: #6c757d;
  cursor: not-allowed;
}

.validate-btn {
  background-color: #28a745;
  color: #fff;
}

.validate-btn:disabled {
  background-color: #e9ecef;
  color: #6c757d;
  cursor: not-allowed;
}

.menu-section,
.order-section {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.success-notification {
  position: fixed;
  bottom: 16px;
  right: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background-color: #28a745;
  color: #fff;
  border-radius: 4px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
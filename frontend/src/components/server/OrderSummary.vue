<template>
  <div class="order-summary">
    <div class="order-summary-header">
      <h3>Récapitulatif de la commande</h3>
    </div>
    
    <div class="order-items" v-if="orderItems.length > 0">
      <div 
        v-for="item in orderItems" 
        :key="item.id"
        class="order-item"
      >
        <div class="order-item-header">
          <!-- Ajout de la photo du plat -->
          <img
            v-if="item.imageUrl"
            :src="item.imageUrl"
            alt="photo du plat"
            style="width: 48px; height: 48px; object-fit: cover; margin-right: 12px; border-radius: 6px;"
          />
          <span class="order-item-name">{{ item.name }}</span>
          <span class="order-item-price">{{ formatPrice(item.price * item.quantity) }}</span>
        </div>
        
        <div class="order-item-quantity">
          <button 
            class="quantity-btn decrease" 
            @click="decreaseQuantity(item.id)"
          >-
            <i class="fas fa-minus"></i>
          </button>
          <span class="quantity-value">{{ item.quantity }}</span>
          <button 
            class="quantity-btn increase" 
            @click="increaseQuantity(item.id)"
          >
          +  
          </button>
        </div>
        
        <div class="order-item-actions">
          <button 
            class="instruction-btn" 
            @click="addInstructions(item)"
            :class="{ 'has-instructions': item.specialInstructions }"
          >
            <i class="fas fa-comment-alt"></i>
            <span v-if="!item.specialInstructions">Instructions</span>
            <span v-else>Modifier</span>
          </button>
          
          <button 
            class="remove-btn" 
            @click="removeItem(item.id)"
          >
            <i class="fas fa-trash-alt"></i>
          </button>
        </div>
        
        <div class="order-item-instructions" v-if="item.specialInstructions">
          <p>
            <i class="fas fa-quote-left"></i>
            {{ item.specialInstructions }}
          </p>
        </div>
      </div>
    </div>
    
    <div class="empty-order" v-else>
      <i class="fas fa-shopping-basket"></i>
      <p>La commande est vide</p>
      <p class="empty-order-hint">Ajoutez des plats du menu</p>
    </div>
    
    <div class="order-summary-footer" v-if="orderItems.length > 0">
      <div class="order-total">
        <span>Total</span>
        <span class="order-total-value">{{ formatPrice(totalAmount) }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderSummary',
  props: {
    orderItems: {
      type: Array,
      required: true
    },
    totalAmount: {
      type: Number,
      default: 0
    }
  },
  emits: ['increase-quantity', 'decrease-quantity', 'remove-item', 'add-instructions'],
  setup(props, { emit }) {
    const formatPrice = (price) => {
      return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(price);
    };
    
    const increaseQuantity = (itemId) => {
      emit('increase-quantity', itemId);
    };
    
    const decreaseQuantity = (itemId) => {
      emit('decrease-quantity', itemId);
    };
    
    const removeItem = (itemId) => {
      emit('remove-item', itemId);
    };
    
    const addInstructions = (item) => {
      emit('add-instructions', item);
    };
    
    return {
      formatPrice,
      increaseQuantity,
      decreaseQuantity,
      removeItem,
      addInstructions
    };
  }
}
</script>

<style scoped>
.order-summary {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #f9f9f9;
}

.order-summary-header {
  padding: 1rem;
  border-bottom: 1px solid #ddd;
  background-color: white;
}

.order-summary-header h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #2196f3;
}

.order-items {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem;
}

.order-item {
  background-color: white;
  border-radius: 8px;
  padding: 0.75rem;
  margin-bottom: 0.75rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.order-item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}

.order-item-name {
  font-weight: bold;
  flex: 1;
}

.order-item-price {
  font-weight: bold;
  color: #2196f3;
}

.order-item-quantity {
  display: flex;
  align-items: center;
  margin-bottom: 0.5rem;
}

.quantity-btn {
  width: 28px;
  height: 28px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
}

.quantity-btn.decrease {
  color: #f44336;
}

.quantity-btn.increase {
  color: #4caf50;
}

.quantity-value {
  width: 30px;
  text-align: center;
  font-weight: bold;
}

.order-item-actions {
  display: flex;
  justify-content: space-between;
}

.instruction-btn, .remove-btn {
  padding: 6px 10px;
  border-radius: 4px;
  border: none;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 0.8rem;
}

.instruction-btn {
  color: #757575;
}

.instruction-btn.has-instructions {
  color: #2196f3;
}

.remove-btn {
  color: #f44336;
}

.order-item-instructions {
  margin-top: 0.75rem;
  padding: 0.5rem;
  background-color: #f5f5f5;
  border-left: 3px solid #2196f3;
  font-size: 0.9rem;
  border-radius: 4px;
}

.order-item-instructions p {
  margin: 0;
  font-style: italic;
  color: #666;
}

.order-item-instructions i {
  margin-right: 5px;
  color: #2196f3;
  font-size: 0.8rem;
}

.empty-order {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  color: #9e9e9e;
  text-align: center;
  height: 100%;
}

.empty-order i {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-order-hint {
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

.order-summary-footer {
  border-top: 1px solid #ddd;
  padding: 1rem;
  background-color: white;
}

.order-total {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  font-size: 1.1rem;
}

.order-total-value {
  color: #2196f3;
}

@media (max-width: 768px) {
  .order-item-actions {
    flex-wrap: wrap;
    gap: 0.5rem;
  }
  
  .instruction-btn, .remove-btn {
    flex: 1;
    justify-content: center;
  }
}
</style>
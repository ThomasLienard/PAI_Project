<template>
  <div class="new-order">
    <form @submit.prevent="submitOrder" class="order-form">
      <div class="form-section">
        <label for="tableNumber">Numéro de table</label>
        <input 
          id="tableNumber"
          v-model="order.tableNumber"
          type="number"
          required
        />
      </div>

      <div class="form-section">
        <label for="clientName">Nom du client (optionnel)</label>
        <input 
          id="clientName"
          v-model="order.clientName"
          type="text"
        />
      </div>

      <div class="form-section">
        <h3>Articles</h3>
        <div class="items-list">
          <div v-for="(item, index) in order.items" :key="index" class="item-entry">
            <select v-model="item.type" required>
              <option value="">Sélectionner le type</option>
              <option value="plat">Plat</option>
              <option value="boisson">Boisson</option>
            </select>
            <select v-model="item.id" required>
              <option value="">Sélectionner l'article</option>
              <option 
                v-for="menuItem in menuItems[item.type]"
                :key="menuItem.id"
                :value="menuItem.id"
              >
                {{ menuItem.name }}
              </option>
            </select>
            <input 
              type="number" 
              v-model="item.quantity"
              min="1"
              required
            />
            <button type="button" @click="removeItem(index)" class="remove-btn">
              ✕
            </button>
          </div>
        </div>
        <button type="button" @click="addItem" class="add-btn">
          Ajouter un article
        </button>
      </div>

      <div class="form-actions">
        <button type="button" @click="resetForm" class="reset-btn">
          Réinitialiser
        </button>
        <button type="submit" class="submit-btn">
          Envoyer la commande
        </button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: 'NewOrder',
  data() {
    return {
      order: {
        tableNumber: '',
        clientName: '',
        items: [{ type: '', id: '', quantity: 1 }]
      },
      menuItems: {
        plat: [], // Sera rempli depuis l'API
        boisson: [] // Sera rempli depuis l'API
      }
    }
  },
  methods: {
    addItem() {
      this.order.items.push({ type: '', id: '', quantity: 1 })
    },
    removeItem(index) {
      this.order.items.splice(index, 1)
    },
    resetForm() {
      this.order = {
        tableNumber: '',
        clientName: '',
        items: [{ type: '', id: '', quantity: 1 }]
      }
    },
    async submitOrder() {
      try {
        this.$emit('orderSubmitted', this.order)
        this.resetForm()
      } catch (error) {
        console.error('Erreur lors de la soumission:', error)
      }
    },
    async fetchMenuItems() {
      try {
        const response = await this.$axios.get('/api/menu')
        this.menuItems = response.data
      } catch (error) {
        console.error('Erreur lors du chargement du menu:', error)
      }
    }
  },
  created() {
    this.fetchMenuItems()
  }
}
</script>

<style scoped>
.order-form {
  max-width: 800px;
  margin: 0 auto;
}

.form-section {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #2c3e50;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
}

.items-list {
  margin-bottom: 15px;
}

.item-entry {
  display: grid;
  grid-template-columns: 1fr 2fr 100px 40px;
  gap: 10px;
  margin-bottom: 10px;
}

.add-btn, .submit-btn, .reset-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-btn {
  background-color: #42b983;
  color: white;
  width: 100%;
}

.remove-btn {
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.submit-btn {
  background-color: #42b983;
  color: white;
}

.reset-btn {
  background-color: #666;
  color: white;
}

button:hover {
  opacity: 0.9;
}
</style>
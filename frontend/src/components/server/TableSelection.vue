<template>
  <div class="table-selection-overlay">
    <div class="table-selection-modal">
      <div class="modal-header">
        <h2>Sélectionnez une table</h2>
        <button class="close-btn" @click="$emit('close')" v-if="!required">
          <i class="fas fa-times"></i>
        </button>
      </div>
      
      <div v-if="loading" class="loading-container">
        <div class="spinner"></div>
        <p>Chargement des tables...</p>
      </div>
      
      <div v-else-if="error" class="error-container">
        <i class="fas fa-exclamation-triangle"></i>
        <p>{{ error }}</p>
        <button class="retry-btn" @click="loadTables">Réessayer</button>
      </div>
      
      <template v-else>
        <div class="tables-status-legend">
          <div class="status-item">
            <div class="status-indicator available"></div>
            <span>Disponible</span>
          </div>
          <div class="status-item">
            <div class="status-indicator occupied"></div>
            <span>Occupée</span>
          </div>
          <div class="status-item">
            <div class="status-indicator reserved"></div>
            <span>Réservée</span>
          </div>
        </div>

        <div class="tables-grid">
          <div 
            v-for="table in tables" 
            :key="table.id"
            class="table-item"
            :class="{ 
              'occupied': table.status === 'OCCUPIED',
              'reserved': table.status === 'RESERVED',
              'available': table.status === 'AVAILABLE'
            }"
            @click="selectTable(table)"
          >
            <span class="table-number">{{ table.numero }}</span>
            <span class="table-capacity">{{ table.capacite }} pers.</span>
            <span class="order-count" v-if="table.orderCount && table.orderCount > 0">
              {{ table.orderCount }} commande(s)
            </span>
          </div>
        </div>
        
        <div v-if="tables.length === 0" class="no-tables">
          <p>Aucune table disponible actuellement</p>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import apiClient from '@/services/apiClient';

export default {
  name: 'TableSelection',
  props: {
    required: {
      type: Boolean,
      default: true
    }
  },
  emits: ['select-table', 'close'],
  setup(props, { emit }) {
    const tables = ref([]);
    const loading = ref(true);
    const error = ref(null);

    const loadTables = async () => {
      loading.value = true;
      error.value = null;
      try {
        const response = await apiClient.get('/tables/all');
        for (const table of response.data) {
          try {
            const orderResponse = await apiClient.get(`/server/orders/tables/${table.id}`);
            table.orderCount = orderResponse.data.length || 0;
            if (table.orderCount > 0 && !table.status) {
              table.status = 'OCCUPIED';
            } else if (!table.status) {
              table.status = 'AVAILABLE';
            }
          } catch (err) {
            console.error(`Erreur lors de la récupération des commandes pour la table ${table.id}:`, err);
            table.orderCount = 0;
          }
        }
        tables.value = response.data;
      } catch (err) {
        console.error('Erreur lors du chargement des tables:', err);
        error.value = 'Impossible de charger les tables. Veuillez réessayer.';
      } finally {
        loading.value = false;
      }
    };

    const selectTable = (table) => {
      emit('select-table', table);
    };

    onMounted(() => {
      loadTables();
    });

    return {
      tables,
      loading,
      error,
      loadTables,
      selectTable
    };
  }
}
</script>

<style scoped>
.table-selection-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.table-selection-modal {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 80vh;
  overflow-y: auto;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #666;
}

.loading-container, .error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  text-align: center;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-container i {
  color: #e74c3c;
  font-size: 2rem;
  margin-bottom: 1rem;
}

.retry-btn {
  margin-top: 1rem;
  padding: 0.5rem 1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.tables-status-legend {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-indicator {
  width: 16px;
  height: 16px;
  border-radius: 50%;
}

.status-indicator.available {
  background-color: #4caf50;
}

.status-indicator.occupied {
  background-color: #f44336;
}

.status-indicator.reserved {
  background-color: #ff9800;
}

.tables-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 1rem;
}

.table-item {
  position: relative;
  border-radius: 8px;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100px;
  transition: transform 0.2s;
  cursor: pointer;
}

.table-item:hover {
  transform: translateY(-3px);
}

.table-item.available {
  background-color: #e8f5e9;
  border: 2px solid #4caf50;
}

.table-item.occupied {
  background-color: #ffebee;
  border: 2px solid #f44336;
}

.table-item.reserved {
  background-color: #fff3e0;
  border: 2px solid #ff9800;
}

.table-number {
  font-size: 1.5rem;
  font-weight: bold;
}

.table-capacity {
  font-size: 0.9rem;
  color: #666;
  margin-top: 5px;
}

.order-count {
  position: absolute;
  top: -10px;
  right: -10px;
  background-color: #2196f3;
  color: white;
  font-size: 0.8rem;
  padding: 3px 8px;
  border-radius: 12px;
}

.no-tables {
  text-align: center;
  padding: 2rem;
  color: #666;
}
</style>
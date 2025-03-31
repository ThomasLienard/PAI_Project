<template>
  <div>
    <div v-if="loading" class="loading">
      <p>Recherche des tables disponibles...</p>
    </div>
    
    <div v-else-if="tables.length > 0" class="tables-list">
      <h2>Tables disponibles</h2>
      <div class="table-items">
        <div v-for="table in tables" :key="table.id" class="table-item">
          <div class="table-info">
            <h3>Table {{ table.numero }}</h3>
            <p>Capacité: {{ table.capacite }} personnes</p>
          </div>
          <button @click="$emit('reserve', table)" class="reserve-button">Réserver</button>
        </div>
      </div>
    </div>
    
    <div v-else-if="hasSearched" class="no-tables">
      <p>Aucune table disponible pour les critères sélectionnés.</p>
      <p>Veuillez essayer une autre date, un autre créneau ou un nombre de personnes différent.</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  tables: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  hasSearched: {
    type: Boolean,
    default: false
  }
});

defineEmits(['reserve']);
</script>

<style scoped>
.loading {
  text-align: center;
  padding: 20px;
  font-style: italic;
  color: #666;
}

.no-tables {
  text-align: center;
  padding: 20px;
  background-color: #f8d7da;
  border-radius: 8px;
  color: #721c24;
}

.tables-list {
  margin-top: 20px;
}

.tables-list h2 {
  margin-bottom: 15px;
  color: #333;
}

.table-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
}

.table-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.table-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.table-info {
  margin-bottom: 15px;
}

.table-info h3 {
  margin: 0 0 10px 0;
  color: #007bff;
}

.reserve-button {
  background-color: #007bff;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.reserve-button:hover {
  background-color: #0069d9;
}
</style>
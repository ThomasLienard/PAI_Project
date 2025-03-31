<template>
  <div class="search-form">
    <h2>Trouver une table disponible</h2>
    
    <div class="form-group">
      <label for="dateReservation">Date de réservation</label>
      <input 
        type="date" 
        id="dateReservation" 
        v-model="criteria.date" 
        :min="todayFormatted"
        @change="emitSearch"
        required
      />
    </div>
    
    <div class="form-group">
      <label>Créneau horaire</label>
      <div class="radio-group">
        <label>
          <input 
            type="radio" 
            value="midi" 
            v-model="criteria.creneau" 
            @change="emitSearch"
            required
          />
          Midi
        </label>
        <label>
          <input 
            type="radio" 
            value="soir" 
            v-model="criteria.creneau" 
            @change="emitSearch"
            required
          />
          Soir
        </label>
      </div>
    </div>
    
    <div class="form-group">
      <label for="nbPersonne">Nombre de personnes</label>
      <input 
        type="number" 
        id="nbPersonne" 
        v-model="criteria.nbPersonnes" 
        min="1" 
        @change="emitSearch"
        required
      />
    </div>
    
    <button @click="emitSearch" class="search-button">Rechercher</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';

const emit = defineEmits(['search']);

const criteria = ref({
  date: '',
  creneau: 'midi',
  nbPersonnes: 1
});

// Date d'aujourd'hui formatée pour l'input date
const todayFormatted = computed(() => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
});

// Initialisation avec la date d'aujourd'hui
onMounted(() => {
  criteria.value.date = todayFormatted.value;
  // Recherche initiale
  emitSearch();
});

// Émettre l'événement de recherche
const emitSearch = () => {
  if (criteria.value.date && criteria.value.creneau && criteria.value.nbPersonnes) {
    emit('search', { ...criteria.value });
  }
};
</script>

<style scoped>
.search-form {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input, select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-group label {
  display: flex;
  align-items: center;
  font-weight: normal;
}

.radio-group input {
  width: auto;
  margin-right: 5px;
}

.search-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  width: 100%;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #45a049;
}
</style>
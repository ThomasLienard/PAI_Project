<template>
  <div class="reservations-list">
    <h2>Mes réservations</h2>
    
    <div v-if="loading" class="loading">
      Chargement de vos réservations...
    </div>
    
    <div v-else-if="reservations.length > 0" class="reservations-grid">
      <div v-for="reservation in reservations" :key="reservation.id" class="reservation-card">
        <div class="reservation-header">
          <h3>Réservation #{{ reservation.id }}</h3>
          <span class="date-badge">{{ formatDate(reservation.dateReservation) }}</span>
        </div>
        
        <div class="reservation-details">
          <p><strong>Créneau:</strong> {{ formatCreneau(reservation.creneauHoraire) }}</p>
          <p><strong>Personnes:</strong> {{ reservation.nbPersonne }}</p>
          <p v-if="reservation.table"><strong>Table:</strong> {{ reservation.table.numero }}</p>
        </div>
        
        <div class="reservation-actions">
          <button @click="$emit('cancel', reservation.id)" class="cancel-button">Annuler</button>
        </div>
      </div>
    </div>
    
    <div v-else class="no-reservations">
      <p>Vous n'avez pas de réservation.</p>
      <p>Utilisez le formulaire ci-dessus pour réserver une table.</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  reservations: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
});

defineEmits(['cancel']);

// Formatage de la date
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const [year, month, day] = dateString.split('-');
  return `${day}/${month}/${year}`;
};

// Formatage du créneau
const formatCreneau = (creneau) => {
  if (!creneau) return '';
  
  return creneau === 'midi' ? 'Midi' : 'Soir';
};
</script>

<style scoped>
.reservations-list {
  margin: 30px 0;
}

.reservations-list h2 {
  margin-bottom: 20px;
  color: #333;
}

.loading {
  text-align: center;
  padding: 20px;
  font-style: italic;
  color: #666;
}

.no-reservations {
  text-align: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  color: #666;
}

.reservations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.reservation-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.reservation-header {
  background-color: #f8f9fa;
  padding: 12px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.reservation-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #333;
}

.date-badge {
  background-color: #007bff;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.85rem;
}

.reservation-details {
  padding: 15px;
}

.reservation-details p {
  margin: 8px 0;
}

.reservation-actions {
  padding: 15px;
  border-top: 1px solid #eee;
  text-align: right;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cancel-button:hover {
  background-color: #c82333;
}
</style>
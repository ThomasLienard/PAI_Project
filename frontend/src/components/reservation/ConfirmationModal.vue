<template>
  <div v-if="show" class="confirmation-modal">
    <div class="modal-content">
      <h2>Confirmer votre réservation</h2>
      
      <div class="reservation-details">
        <p><strong>Date:</strong> {{ formattedDate }}</p>
        <p><strong>Créneau:</strong> {{ reservation.creneau === 'midi' ? 'Midi' : 'Soir' }}</p>
        <p><strong>Nombre de personnes:</strong> {{ reservation.nbPersonnes }}</p>
        <p><strong>Table:</strong> {{ table.numero }} (capacité: {{ table.capacite }} personnes)</p>
      </div>
      
      <div class="modal-actions">
        <button @click="$emit('confirm')" class="confirm-button">Confirmer</button>
        <button @click="$emit('cancel')" class="cancel-button">Annuler</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    required: true
  },
  reservation: {
    type: Object,
    required: true
  },
  table: {
    type: Object,
    default: () => ({})
  }
});

defineEmits(['confirm', 'cancel']);

// Formatage de la date pour un affichage plus lisible
const formattedDate = computed(() => {
  if (!props.reservation.date) return '';
  
  const [year, month, day] = props.reservation.date.split('-');
  return `${day}/${month}/${year}`;
});
</script>

<style scoped>
.confirmation-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background-color: white;
  padding: 30px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.modal-content h2 {
  margin-top: 0;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.reservation-details {
  margin-bottom: 20px;
}

.reservation-details p {
  margin: 8px 0;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.confirm-button {
  background-color: #28a745;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.confirm-button:hover {
  background-color: #218838;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cancel-button:hover {
  background-color: #c82333;
}
</style>
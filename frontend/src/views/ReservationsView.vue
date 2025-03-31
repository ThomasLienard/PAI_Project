<script setup>
import NavBar from '../components/NavBar.vue';
import { onMounted, ref } from 'vue';
import ReservationsList from '../components/reservation/ReservationsList.vue';
import apiClient from '../services/apiClient';

const errorMessage = ref('');
const reservations = ref([]);
const loading = ref(false);

const userReservations = async () => {
  loading.value = true;
  try {
    const clientId = sessionStorage.getItem('user');
    const response = await apiClient.get("user/reservation/getAll", {
      params: { clientId } 
    });
    reservations.value = response.data;
  } catch (error) {
    console.log(error);
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message;
    } 
    else {
      errorMessage.value = 'Erreur lors de la récupération des réservations';
    }
  } finally {
    loading.value = false;
  }
}

const cancelReservation = async (reservationId) => {
  if (!confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
    return;
  }
  
  try {
    await apiClient.delete(`/user/reservation/delete/${reservationId}`);
    // Mettre à jour la liste après suppression
    userReservations();
  } catch (error) {
    console.error('Erreur lors de l\'annulation:', error);
    errorMessage.value = error.response?.data || 'L\'annulation a échoué';
  }
};

onMounted(() => {
  userReservations();
});
</script>

<template>
  <div>
    <NavBar />
    <main class="reservations-container">
      <h1>Mes réservations</h1>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      
      <div class="actions">
        <button @click="$router.push('/user/tables-disponibles')" class="new-reservation-btn">
          Nouvelle réservation
        </button>
      </div>
      
      <ReservationsList 
        :reservations="reservations"
        :loading="loading"
        @cancel="cancelReservation"
      />
    </main>
  </div>
</template>

<style scoped>
.reservations-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.error {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 20px;
  text-align: center;
}

.actions {
  margin-bottom: 20px;
  text-align: right;
}

.new-reservation-btn {
  background-color: #28a745;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.new-reservation-btn:hover {
  background-color: #218838;
}
</style>
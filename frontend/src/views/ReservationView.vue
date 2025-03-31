<template>
  <div>
    <NavBar />
    <main class="disponibilite-container">
      <h1>Réservation de table</h1>
      
      <!-- Afficher un message d'erreur global si nécessaire -->
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      
      <!-- Formulaire de recherche -->
      <SearchTableForm @search="searchTables" />
      
      <!-- Liste des tables disponibles -->
      <AvailableTablesList 
        :tables="availableTables"
        :loading="loading"
        :hasSearched="hasSearched"
        @reserve="reserveTable"
      />
      
      <!-- Liste des réservations existantes -->
      <ReservationsList 
        :reservations="userReservations"
        :loading="loadingReservations"
        @cancel="cancelReservation"
      />
      
      <!-- Modal de confirmation -->
      <ConfirmationModal
        :show="showConfirmation"
        :reservation="searchCriteria"
        :table="selectedTable"
        @confirm="confirmReservation"
        @cancel="cancelConfirmation"
      />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import NavBar from '../components/NavBar.vue';
import SearchTableForm from '../components/reservation/SearchTableForm.vue';
import AvailableTablesList from '../components/reservation/AvailableTablesList.vue';
import ReservationsList from '../components/reservation/ReservationsList.vue';
import ConfirmationModal from '../components/reservation/ConfirmationModal.vue';
import apiClient from '../services/apiClient';

const router = useRouter();
const loading = ref(false);
const loadingReservations = ref(false);
const hasSearched = ref(false);
const errorMessage = ref('');
const availableTables = ref([]);
const userReservations = ref([]);
const showConfirmation = ref(false);
const selectedTable = ref(null);
const searchCriteria = ref({
  date: '',
  creneau: 'midi',
  nbPersonnes: 1
});

// Recherche des tables disponibles
const searchTables = async (criteria) => {
  searchCriteria.value = criteria;
  
  loading.value = true;
  hasSearched.value = true;
  errorMessage.value = '';
  
  try {
    const response = await apiClient.get('/tables/disponibles', {
      params: {
        date: criteria.date,
        creneau: criteria.creneau,
        nbPersonnes: criteria.nbPersonnes
      }
    });
    
    availableTables.value = response.data;
  } catch (error) {
    console.error('Erreur lors de la recherche des tables:', error);
    errorMessage.value = error.response?.data || 'Erreur lors de la recherche des tables disponibles';
  } finally {
    loading.value = false;
  }
};

// Récupération des réservations de l'utilisateur
const fetchUserReservations = async () => {
  loadingReservations.value = true;
  
  try {
    const clientId = sessionStorage.getItem('user');
    const response = await apiClient.get("user/reservation/getAll", {
      params: { clientId } 
    });
    userReservations.value = response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des réservations:', error);
    errorMessage.value = error.response?.data || 'Erreur lors de la récupération des réservations';
  } finally {
    loadingReservations.value = false;
  }
};

// Afficher la modale de confirmation
const reserveTable = (table) => {
  selectedTable.value = table;
  showConfirmation.value = true;
};

// Confirmer la réservation
const confirmReservation = async () => {
  loading.value = true;
  errorMessage.value = '';
  
  try {
    await apiClient.post('/user/reservation/create', {
      dateReservation: searchCriteria.value.date,
      creneauHoraire: searchCriteria.value.creneau,
      nbPersonne: searchCriteria.value.nbPersonnes,
      client: sessionStorage.getItem('user'),
      tableId: selectedTable.value.id
    });
    
    // Fermer la modale
    showConfirmation.value = false;
    
    // Rafraîchir les tables disponibles et les réservations
    await searchTables(searchCriteria.value);
    await fetchUserReservations();
    
  } catch (error) {
    console.error('Erreur lors de la réservation:', error);
    errorMessage.value = error.response?.data || 'La réservation a échoué';
  } finally {
    loading.value = false;
  }
};

// Annuler la confirmation
const cancelConfirmation = () => {
  showConfirmation.value = false;
  selectedTable.value = null;
};

// Annuler une réservation
const cancelReservation = async (reservationId) => {
  if (!confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
    return;
  }
  
  loadingReservations.value = true;
  errorMessage.value = '';
  
  try {
    await apiClient.delete(`/user/reservation/delete/${reservationId}`);
    
    // Rafraîchir les tables disponibles et les réservations
    await searchTables(searchCriteria.value);
    await fetchUserReservations();
    
  } catch (error) {
    console.error('Erreur lors de l\'annulation de la réservation:', error);
    errorMessage.value = error.response?.data || 'L\'annulation de la réservation a échoué';
  } finally {
    loadingReservations.value = false;
  }
};

onMounted(async () => {
  // Charger les réservations de l'utilisateur
  fetchUserReservations();
});
</script>

<style scoped>
.disponibilite-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 20px;
  text-align: center;
}
</style>
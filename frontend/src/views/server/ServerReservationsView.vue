<template>
  <div>
    <NavBar />
    <main class="reservations-container">
      <h1>Réservations du jour</h1>
      
      <!-- Afficher un message d'erreur global si nécessaire -->
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      
      <!-- Statistiques des réservations -->
      <div class="stats-container">
        <div class="stat-card" :class="{ 'high-occupancy': occupancyRate > 80, 'medium-occupancy': occupancyRate > 50 && occupancyRate <= 80 }">
          <div class="stat-value">{{ occupancyRate }}%</div>
          <div class="stat-label">Taux d'occupation</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ reservations.length }}</div>
          <div class="stat-label">Total réservations</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ totalGuests }}</div>
          <div class="stat-label">Clients attendus</div>
        </div>
      </div>
      
      <!-- Options de filtrage -->
      <div class="filter-options">
        <div class="search-wrapper">
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="Rechercher par nom de client..."
            class="search-input"
          />
          <i class="fa fa-search search-icon"></i>
        </div>
        
        <div class="filter-selects">
          <select v-model="timeSlotFilter" class="filter-select">
            <option value="">Tous les créneaux</option>
            <option value="midi">Déjeuner</option>
            <option value="soir">Dîner</option>
          </select>
          
          <select v-model="groupSizeFilter" class="filter-select">
            <option value="">Toutes tailles de groupe</option>
            <option value="1-2">1-2 personnes</option>
            <option value="3-4">3-4 personnes</option>
            <option value="5+">5+ personnes</option>
          </select>
        </div>
      </div>

      <!-- Affichage des réservations -->
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>Chargement des réservations...</p>
      </div>
      
      <div v-else>
        <!-- Section Déjeuner -->
        <div v-if="lunchReservations.length > 0" class="time-slot-section">
          <h2 class="time-slot-title">
            <i class="fas fa-sun"></i> Déjeuner
            <span class="reservation-count">{{ lunchReservations.length }} réservation(s)</span>
          </h2>
          
          <div class="reservations-grid">
            <div v-for="reservation in lunchReservations" :key="reservation.id" class="reservation-card">
              <div class="reservation-header">
                <h3 class="client-name">{{ reservation.clientName || 'Client #' + reservation.id }}</h3>
                <span class="table-badge">Table #{{ reservation.table?.numero || 'N/A' }}</span>
              </div>
              <div class="reservation-details">
                <p><i class="fas fa-users"></i> {{ reservation.nbPersonne }} personne(s)</p>
                <p><i class="fas fa-clock"></i> {{ formatDate(reservation.dateReservation) }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Section Dîner -->
        <div v-if="dinnerReservations.length > 0" class="time-slot-section">
          <h2 class="time-slot-title">
            <i class="fas fa-moon"></i> Dîner
            <span class="reservation-count">{{ dinnerReservations.length }} réservation(s)</span>
          </h2>
          
          <div class="reservations-grid">
            <div v-for="reservation in dinnerReservations" :key="reservation.id" class="reservation-card">
              <div class="reservation-header">
                <h3 class="client-name">{{ reservation.clientName || 'Client #' + reservation.id }}</h3>
                <span class="table-badge">Table #{{ reservation.table?.numero || 'N/A' }}</span>
              </div>
              <div class="reservation-details">
                <p><i class="fas fa-users"></i> {{ reservation.nbPersonne }} personne(s)</p>
                <p><i class="fas fa-clock"></i> {{ formatDate(reservation.dateReservation) }}</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Message si aucune réservation -->
        <div v-if="filteredReservations.length === 0" class="empty-state">
          <i class="fas fa-calendar-times empty-icon"></i>
          <p>Aucune réservation ne correspond à votre recherche</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import NavBar from '@/components/NavBar.vue';
import apiClient from '@/services/apiClient';

// État local
const reservations = ref([]);
const loading = ref(true);
const errorMessage = ref('');
const searchQuery = ref('');
const timeSlotFilter = ref('');
const groupSizeFilter = ref('');
const totalTables = 20; // À ajuster selon le nombre réel de tables

// Calcul des réservations filtrées
const filteredReservations = computed(() => {
  if (!reservations.value) return [];
  
  return reservations.value.filter(reservation => {
    // Filtre par nom de client
    const matchesSearch = !searchQuery.value || 
      (reservation.clientName && 
       reservation.clientName.toLowerCase().includes(searchQuery.value.toLowerCase()));
    
    // Filtre par créneau
    const matchesTimeSlot = !timeSlotFilter.value || 
      reservation.creneauHoraire === timeSlotFilter.value;
    
    // Filtre par taille de groupe
    let matchesGroupSize = true;
    if (groupSizeFilter.value) {
      const size = reservation.nbPersonne;
      if (groupSizeFilter.value === '1-2') {
        matchesGroupSize = size >= 1 && size <= 2;
      } else if (groupSizeFilter.value === '3-4') {
        matchesGroupSize = size >= 3 && size <= 4;
      } else if (groupSizeFilter.value === '5+') {
        matchesGroupSize = size >= 5;
      }
    }
    
    return matchesSearch && matchesTimeSlot && matchesGroupSize;
  });
});

// Séparation des réservations par créneau
const lunchReservations = computed(() => {
  return filteredReservations.value.filter(r => r.creneauHoraire === 'midi');
});

const dinnerReservations = computed(() => {
  return filteredReservations.value.filter(r => r.creneauHoraire === 'soir');
});

// Calcul des statistiques
const occupancyRate = computed(() => {
  if (!reservations.value.length) return 0;
  const uniqueTables = new Set(reservations.value.map(r => r.table?.id).filter(Boolean));
  return Math.round((uniqueTables.size / totalTables) * 100);
});

const totalGuests = computed(() => {
  if (!reservations.value.length) return 0;
  return reservations.value.reduce((total, r) => total + (r.nbPersonne || 0), 0);
});

// Chargement des réservations
const fetchReservations = async () => {
  loading.value = true;
  errorMessage.value = '';
  
  try {
    // Utiliser apiClient pour récupérer les réservations du jour
    const response = await apiClient.get('/server/reservations/today');
    reservations.value = response.data || [];
  } catch (error) {
    console.error('Erreur lors du chargement des réservations:', error);
    errorMessage.value = error.response?.data || 'Erreur lors du chargement des réservations';
    reservations.value = [];
  } finally {
    loading.value = false;
  }
};

// Formatage de la date
const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const parts = dateString.split('-');
  if (parts.length !== 3) return dateString;
  
  return `${parts[2]}/${parts[1]}/${parts[0]}`;
};

// Au chargement du composant
onMounted(() => {
  fetchReservations();
});
</script>

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

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px 15px;
  border-radius: 4px;
  margin-bottom: 20px;
  text-align: center;
}

/* Styles pour les statistiques */
.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 0.9rem;
  color: #666;
  margin-top: 5px;
}

.high-occupancy .stat-value {
  color: #e74c3c;
}

.medium-occupancy .stat-value {
  color: #f39c12;
}

/* Styles pour les filtres */
.filter-options {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 30px;
  align-items: center;
}

.search-wrapper {
  flex: 1;
  position: relative;
  min-width: 250px;
}

.search-input {
  width: 100%;
  padding: 12px 15px 12px 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.filter-selects {
  display: flex;
  gap: 10px;
}

.filter-select {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
  min-width: 150px;
}

/* Styles pour les sections de créneaux */
.time-slot-section {
  background-color: white;
  border-radius: 8px;
  padding: 25px;
  margin-bottom: 30px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.time-slot-title {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #2c3e50;
  margin: 0 0 20px 0;
  font-size: 1.5rem;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.reservation-count {
  font-size: 0.9rem;
  color: #666;
  font-weight: normal;
  margin-left: auto;
}

/* Styles pour la grille de réservations */
.reservations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.reservation-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  transition: transform 0.2s, box-shadow 0.2s;
}

.reservation-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.reservation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.client-name {
  margin: 0;
  color: #2c3e50;
  font-size: 1.2rem;
}

.table-badge {
  background-color: #42b983;
  color: white;
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: bold;
}

.reservation-details {
  color: #555;
  font-size: 1rem;
}

.reservation-details p {
  margin: 10px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.reservation-details i {
  color: #666;
  width: 16px;
}

/* Styles pour les états de chargement et vide */
.loading-container {
  text-align: center;
  padding: 50px 0;
  color: #666;
}

.loading-spinner {
  display: inline-block;
  width: 40px;
  height: 40px;
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top-color: #42b983;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

.empty-state {
  text-align: center;
  padding: 50px 0;
  color: #666;
}

.empty-icon {
  font-size: 4rem;
  color: #ddd;
  margin-bottom: 20px;
}

/* Animation pour le spinner */
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Styles pour les écrans plus petits */
@media (max-width: 768px) {
  .reservations-container {
    padding: 15px;
  }
  
  .filter-options {
    flex-direction: column;
  }
  
  .filter-selects {
    width: 100%;
  }
  
  .filter-select {
    flex: 1;
  }
  
  .reservations-grid {
    grid-template-columns: 1fr;
  }
}
</style>
<template>
  <div>
    <NavBar />
    <div class="server-reservations">
      <div class="header">
        <h1>Réservations</h1>
        <router-link to="/server" class="back-btn">
          <i class="fas fa-arrow-left"></i> Retour
        </router-link>
      </div>

      <div class="search-bar" v-if="!loading && reservations.length > 0">
        <input 
          v-model="searchQuery" 
          placeholder="Rechercher une réservation..."
          type="text"
        />
      </div>

      <div v-if="loading" class="loading">
        Chargement des réservations...
      </div>
      
      <div v-else>
        <div v-if="reservations && reservations.length > 0">
          <Reservations 
            :reservations="filteredReservations"
          />
        </div>
        <div v-else class="empty-state">
          <i class="fas fa-calendar empty-icon"></i>
          <p>Aucune réservation pour aujourd'hui</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import Reservations from '@/components/server/Reservations.vue'

export default {
  name: 'ServerReservationsView',
  components: {
    NavBar,
    Reservations
  },
  data() {
    return {
      reservations: [],
      searchQuery: '',
      loading: true
    }
  },
  computed: {
    filteredReservations() {
      if (!this.reservations) return []
      return this.reservations.filter(reservation => {
        const searchLower = this.searchQuery.toLowerCase()
        return (
          reservation.clientName?.toLowerCase().includes(searchLower) ||
          reservation.time?.includes(searchLower)
        )
      })
    }
  },
  async created() {
    try {
      await this.fetchReservations()
    } catch (error) {
      console.error('Erreur lors du chargement des réservations:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    async fetchReservations() {
      try {
        const response = await this.$axios.get('/api/reservations/today')
        this.reservations = response.data || []
      } catch (error) {
        console.error('Erreur:', error)
        this.reservations = []
      }
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.back-btn {
  text-decoration: none;
  color: #42b983;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 1px solid #42b983;
  border-radius: 4px;
  transition: all 0.3s;
}

.back-btn:hover {
  background-color: #42b983;
  color: white;
}

h1 {
  color: #2c3e50;
  margin: 0;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  background: #f8f9fa;
  border-radius: 8px;
  margin-top: 2rem;
}

.empty-icon {
  font-size: 4rem;
  color: #ddd;
  margin-bottom: 1rem;
}

.empty-state p {
  color: #666;
  font-size: 1.2rem;
}

.search-bar {
  margin-bottom: 2rem;
}

.search-bar input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}
</style>
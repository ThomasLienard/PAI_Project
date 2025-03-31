<template>
  <div class="reservations">
    <div class="reservations-grid">
      <div v-for="reservation in reservations" :key="reservation.id" class="reservation-card">
        <div class="reservation-header">
          <h3>{{ reservation.clientName }}</h3>
          <span class="table-number" v-if="reservation.tableNumber">
            Table #{{ reservation.tableNumber }}
          </span>
        </div>
        <div class="reservation-details">
          <p>
            <i class="fas fa-clock"></i>
            {{ formatDateTime(reservation.dateTime) }}
          </p>
          <p>
            <i class="fas fa-users"></i>
            {{ reservation.numberOfPeople }} personnes
          </p>
          <p v-if="reservation.specialRequests" class="special-requests">
            <i class="fas fa-info-circle"></i>
            {{ reservation.specialRequests }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Reservations',
  props: {
    reservations: {
      type: Array,
      required: true
    }
  },
  methods: {
    formatDateTime(dateTime) {
      return new Date(dateTime).toLocaleString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit',
        day: '2-digit',
        month: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.reservations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.reservation-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.reservation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.reservation-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 1.1em;
}

.table-number {
  background-color: #42b983;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9em;
}

.reservation-details p {
  margin: 8px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.special-requests {
  font-style: italic;
  color: #666;
}

i {
  color: #42b983;
}
</style>
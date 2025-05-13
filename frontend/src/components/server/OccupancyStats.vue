<template>
  <div class="dashboard-stats">
    <div class="stat-item" :class="occupancyClass">
      <span class="stat-label">Taux d'occupation</span>
      <span class="stat-value">{{ occupancyRate }}%</span>
    </div>
    <div class="stat-item">
      <span class="stat-label">Total réservations</span>
      <span class="stat-value">{{ totalReservations }}</span>
    </div>
    <div class="stat-item">
      <span class="stat-label">Clients attendus</span>
      <span class="stat-value">{{ totalGuests }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OccupancyStats',
  props: {
    occupancyRate: {
      type: Number,
      default: 0
    },
    totalReservations: {
      type: Number,
      default: 0
    },
    totalGuests: {
      type: Number,
      default: 0
    }
  },
  computed: {
    occupancyClass() {
      if (this.occupancyRate > 80) return 'high-occupancy';
      if (this.occupancyRate > 50) return 'medium-occupancy';
      return '';
    }
  }
}
</script>
<template>
  <div class="time-slot-section">
    <h2>
      <i :class="icon"></i> {{ title }}
      <span class="reservation-count">{{ reservations.length }} réservation(s)</span>
    </h2>
    <div class="reservations-grid">
      <ReservationCard 
        v-for="reservation in reservations" 
        :key="reservation.id" 
        :reservation="reservation" 
      />
    </div>
  </div>
</template>

<script>
import ReservationCard from './ReservationCard.vue';

export default {
  name: 'TimeSlotSection',
  components: {
    ReservationCard
  },
  props: {
    title: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      default: 'fas fa-clock'
    },
    reservations: {
      type: Array,
      default: () => []
    }
  }
}
</script>
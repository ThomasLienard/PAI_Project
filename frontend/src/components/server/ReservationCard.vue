<template>
  <div class="reservation-card">
    <div class="reservation-header">
      <h3>{{ reservation.clientName || 'Client #' + reservation.id }}</h3>
      <span class="table-number">
        Table #{{ reservation.table?.numero || 'N/A' }}
      </span>
    </div>
    <div class="reservation-details">
      <p>
        <i class="fas fa-users"></i>
        {{ reservation.nbPersonne }} personne(s)
      </p>
      <p>
        <i class="fas fa-clock"></i>
        {{ formattedDateTime }}
      </p>
      <p v-if="reservation.specialRequests" class="special-requests">
        <i class="fas fa-info-circle"></i>
        {{ reservation.specialRequests }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReservationCard',
  props: {
    reservation: {
      type: Object,
      required: true
    }
  },
  computed: {
    formattedDateTime() {
      if (!this.reservation.dateReservation) return '';
      
      const [datePart, timePart] = this.reservation.dateReservation.split(' ');
      if (!datePart) return '';
      
      const [year, month, day] = datePart.split('-');
      const formattedDate = `${day}/${month}/${year}`;
      
      return timePart ? `${formattedDate} - ${timePart}` : formattedDate;
    }
  }
}
</script>
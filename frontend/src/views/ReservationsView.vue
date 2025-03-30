<script setup lang="ts">
import NavBar from '../components/NavBar.vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import apiClient from '../services/apiClient';

const errorMessage = ref('');

const reservations = ref([]);

const userReservations = async () => {
  try {
    const clientId = sessionStorage.getItem('user');
    //console.log(clientId);
    const response = await apiClient.get("user/reservation/getAll", {
      params: { clientId } 
    });
    //console.log(response.data);
    reservations.value = response.data;
  } catch (error) {
    console.log(error);
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message;
    } 
    else {
      errorMessage.value = 'Erreur lors de la récupération des réservations';
    }
  }
}

onMounted(() => {
  userReservations();
});
</script>


<template>
  <div>
    <NavBar />
      <main>
        <h1>Mes réservations :</h1>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        
        <ul v-if="reservations.length > 0">
          <li v-for="reservation in reservations" :key="reservation.id">
            <strong>Date :</strong> {{ reservation.dateReservation }}<br />
            <strong>Créneau horaire :</strong> {{ reservation.creneauHoraire }}<br />
            <strong>Nombre de personnes :</strong> {{ reservation.nbPersonne }}
          </li>
        </ul>

        <p v-else>Vous n'avez pas de réservation</p>
      </main>
  </div>
  </template>
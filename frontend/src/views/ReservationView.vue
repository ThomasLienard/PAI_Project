<script setup lang="ts">
import NavBar from '../components/NavBar.vue';
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import apiClient from '../services/apiClient'

const newReservation = ref({
  date_reservation: '',
  creneau_horaire: '',
  nbPersonne: 0
})
const router = useRouter()
const errorMessage = ref('')


const createReservation = async () => {
  try {
    console.log({
      date_reservation: newReservation.value.date_reservation,
      creneau_horaire: newReservation.value.creneau_horaire,
      nbPersonne: newReservation.value.nbPersonne
    });
    const response = await apiClient.post("user/reservation/create", newReservation.value);
    console.log("requête effectuée", response.data);
    newReservation.value = {
      date_reservation: '',
      creneau_horaire: '',
      nbPersonne: 0
    };
    console.log("paramètres réinitialisés");
    router.push('/user/reservations')
  } 
  catch (error) {
    console.log(error);
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message;
    } 
    else {
      errorMessage.value ='Erreur lors de la création de la réservation';
    }
  }
};
</script>

<template>
  <div>
    <NavBar />
    <main>
      <div>
        <form @submit.prevent="createReservation">
          <input type="date" v-model="newReservation.date_reservation" required>
          <input type="time" v-model="newReservation.creneau_horaire" required>
          <input type="number" placeholder="Nombre de personnes" v-model="newReservation.nbPersonne" required>
          <button type="submit">Vérifier la réservation</button>
        </form>
        
        <div v-if="errorMessage" class="error">{{ errorMessage }}</div>

      </div>
      
    </main>
  </div>
</template>

<script setup lang="ts">
import NavBar from '../components/NavBar.vue';
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import apiClient from '../services/apiClient'

const newReservation = ref({
  dateReservation: '',
  creneauHoraire: '',
  nbPersonne: 0,
  client: sessionStorage.getItem('user')
})
const router = useRouter()
const errorMessage = ref('')


const createReservation = async () => {
  try {
    /*
    console.log( sessionStorage.getItem('user'));
    console.log({
      dateReservation: newReservation.value.dateReservation,
      creneauHoraire: newReservation.value.creneauHoraire,
      nbPersonne: newReservation.value.nbPersonne,
      client: newReservation.value.client
    });
    */
    const response = await apiClient.post("user/reservation/create", newReservation.value);
    newReservation.value = {
      dateReservation: '',
      creneauHoraire: '',
      nbPersonne: 0,
      client: ""
    };
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
          <input type="date" v-model="newReservation.dateReservation" required>
            <div>
            <label>
              <input type="radio" value="midi" v-model="newReservation.creneauHoraire" required>
              Midi
            </label>
            <label>
              <input type="radio" value="soir" v-model="newReservation.creneauHoraire" required>
              Soir
            </label>
            </div>
          <input type="number" placeholder="Nombre de personnes" v-model="newReservation.nbPersonne" required>
          <button type="submit">Vérifier la réservation</button>
        </form>
        
        <div v-if="errorMessage" class="error">{{ errorMessage }}</div>

      </div>
      
    </main>
  </div>
</template>

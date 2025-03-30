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
});
const router = useRouter();
const errorMessage = ref('');

const nbCreneauxDisponibles = ref(10); // valeur définit clairement mais dépendra du nombre de tables et réservation pour le créneau choisi


const createReservation = async () => {
  if (nbCreneauxDisponibles.value <= 0) {
    errorMessage.value = "Aucun créneau n'est disponible pour le moment.";
    return;
  }
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
    nbCreneauxDisponibles.value = nbCreneauxDisponibles.value - 1;
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
        <form @submit.prevent="createReservation" class="reservation-form">
          <div class="form-group">
            <label for="dateReservation">Date de réservation</label>
            <input type="date" id="dateReservation" v-model="newReservation.dateReservation" required>
          </div>
          
          <div class="form-group">
            <label>Créneau horaire</label>
            <div class="radio-group">
              <label>
          <input type="radio" value="midi" v-model="newReservation.creneauHoraire" required>
          Midi
              </label>
              <label>
          <input type="radio" value="soir" v-model="newReservation.creneauHoraire" required>
          Soir
              </label>
            </div>
          </div>
          
          <div class="form-group">
            <label for="nbPersonne">Nombre de personnes</label>
            <input type="number" id="nbPersonne" placeholder="Nombre de personnes" v-model="newReservation.nbPersonne" required>
          </div>
          
          <div class="form-group">
            <button type="submit" class="submit-button">Vérifier la réservation</button>
          </div>
        </form>
        
        <div v-if="errorMessage" class="error">{{ errorMessage }}</div>

      </div>
      
    </main>
  </div>
</template>

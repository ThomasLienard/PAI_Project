<script setup lang="ts">
import NavBar from '../components/NavBar.vue';
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';


const newReservation = ref({
  date_reservation: '',
  creneau_horaire: '',
  nbPersonne: 0
});
const router = useRouter()

const createReservation = async () => {
  try {
    console.log({
      date_reservation: newReservation.value.date_reservation,
      creneau_horaire: newReservation.value.creneau_horaire,
      nbPersonne: newReservation.value.nbPersonne
    });
    const response = await axios.post("http://localhost:8080/api/reservation/create", {
      date_reservation: newReservation.value.date_reservation,
      creneau_horaire: newReservation.value.creneau_horaire,
      nbPersonne: newReservation.value.nbPersonne
    });
    newReservation.value = {
      date_reservation: '',
      creneau_horaire: '',
      nbPersonne: 0
    };
    router.push('/user/reservations')
  } 
  catch (error) {
    console.log(error);
    if (error.response && error.response.data && error.response.data.message) {
      console.log(error.response.data.message);
    } 
    else {
      console.log('Erreur lors de la création de la réservation');
    }
  }
};
</script>

<template>
  <div>
    <NavBar />
    <main>
      <form @submit.prevent="createReservation">
          <input type="date" v-model="newReservation.date">
          <input type="time" v-model="newReservation.time">
          <input type="number" placeholder="Nombre de personnes" v-model="newReservation.nbPersonnes">
          <button type="submit">Vérifier la réservation</button>
      </form>
      

    </main>
  </div>
</template>

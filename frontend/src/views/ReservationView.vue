<script setup lang="ts">
import NavBar from '../components/NavBar.vue';
</script>

<template>
  <div>
    <NavBar />
    <main>
      <form action="/user/reservation/choix">
          <input type="date" name="" id="">
          <input type="time" name="" id="">
          <input type="number" placeholder="Nombre de personnes" id="">
          <button type="submit">Vérifier la réservation</button>
      </form>
      

    </main>
  </div>
</template>

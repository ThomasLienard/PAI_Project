<script setup lang="ts">
import NavBar from '../components/NavBar.vue';
</script>

<template>
  <div>
    <NavBar />
      <main>
        <div>
          <form action="/user/reservation">
            <button type="submit">
              Réserver une table
            </button>
          </form>
        </div>
        <div>
          <form action="/user/reservations">
            <button type="submit">
              Voir mes réservations
            </button>
          </form>
        </div>

        <div>
          <form action="/user/commander">
            <button type="submit">
              Commander 
            </button>
          </form>
        </div>

        <div>
          <form action="/user/commandes">
            <button type="submit">
              Voir mes commandes 
            </button>
          </form>
        </div>
        
        <div>
          <form action="/user/modifier">
            <button type="submit">
              Afficher son profil 
            </button>
          </form>
        </div>
        
        <div>
          <form action="/user/feedback">
            <button type="submit">
              Laisser un avis
            </button>
          </form>
        </div> 
      </main>
  </div>
</template>

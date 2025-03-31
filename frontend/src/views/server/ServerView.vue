<template>
  <div>
    <NavBar />
    <div class="server-view">
      <h1>Interface Serveur</h1>
      
      <div class="navigation-grid">
        <router-link to="/server/orders" class="nav-card">
          <i class="fas fa-clipboard-list"></i>
          <h2>Commandes en cours</h2>
          <p>Gérer et suivre les commandes actuelles</p>
        </router-link>

        <router-link to="/server/reservations" class="nav-card">
          <i class="fas fa-calendar-alt"></i>
          <h2>Réservations</h2>
          <p>Consulter les réservations du jour</p>
        </router-link>

        <router-link to="/server/new-order" class="nav-card">
          <i class="fas fa-plus-circle"></i>
          <h2>Nouvelle Commande</h2>
          <p>Créer une nouvelle commande</p>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'

export default {
  name: 'ServerView',
  components: {
    NavBar
  }
}
</script>

<style scoped>
.server-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.navigation-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.nav-card {
  text-decoration: none;
  color: inherit;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.nav-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.nav-card i {
  font-size: 2.5em;
  color: #42b983;
  margin-bottom: 15px;
}

h1 {
  color: #2c3e50;
  margin-bottom: 30px;
}

h2 {
  color: #2c3e50;
  margin: 15px 0;
}

p {
  color: #666;
}
</style>
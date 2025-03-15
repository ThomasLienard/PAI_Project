<template>
  <div>
    <h1>Gestion des utilisateurs</h1>
    <form @submit.prevent="createUser">
      <input v-model="newUser.username" placeholder="Nom d'utilisateur" required />
      <input v-model="newUser.email" placeholder="Email" required />
      <input v-model="newUser.password" type="password" placeholder="Mot de passe" required />
      <select v-model="newUser.role" required>
        <option value="SERVEUR">Serveur</option>
        <option value="CUISINIER">Cuisinier</option>
      </select>
      <button type="submit">Créer un utilisateur</button>
    </form>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.username }} - {{ user.email }} - Rôle: {{ user.role }}
        <button @click="editUser(user)">Modifier</button>
        <button @click="deleteUser(user.id)">Supprimer</button>
      </li>
    </ul>
    <div>
      <h2>Activités des employés</h2>
      <ul>
        <li v-for="activity in activities" :key="activity.id">
          {{ activity.username }} - {{ activity.action }} - {{ activity.timestamp }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const users = ref([])
const newUser = ref({ username: '', email: '', password: '', role: '' })
const activities = ref([])

const fetchUsers = async () => {
  const response = await axios.get('/api/users')
  users.value = response.data
}

const createUser = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/admin/create-user', newUser.value)
    console.log('Utilisateur créé avec succès:', response.data)
    newUser.value = { username: '', email: '', password: '', role: '' }
    fetchUsers()
    fetchActivities()
  } catch (error) {
    console.error('Erreur lors de la création de l\'utilisateur:', error.response.data)
  }
}

const editUser = (user) => {
  newUser.value = { ...user }
}

const deleteUser = async (id) => {
  await axios.delete(`/api/users/${id}`)
  fetchUsers()
  fetchActivities()
}

const fetchActivities = async () => {
  const response = await axios.get('/api/activities')
  activities.value = response.data
}

fetchUsers()
fetchActivities()
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

input, select, button {
  padding: 0.5rem;
  font-size: 1rem;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid #ccc;
}

button {
  margin-left: 1rem;
}
</style>
<template>
  <div>
    <NavBar />
    <div class="container">
      <h1>Gestion des utilisateurs</h1>
      
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>

      <form @submit.prevent="createUser" class="user-form">
        <div class="form-group">
          <input 
            v-model="newUser.username" 
            placeholder="Nom d'utilisateur" 
            required 
          />
        </div>
        <div class="form-group">
          <input 
            v-model="newUser.email" 
            type="email" 
            placeholder="Email" 
            required 
          />
        </div>
        <div class="form-group">
          <input 
            v-model="newUser.password" 
            type="password" 
            placeholder="Mot de passe" 
            required 
          />
        </div>
        <div class="form-group">
          <select v-model="newUser.role" required>
            <option value="">Sélectionner un rôle</option>
            <option value="SERVEUR">Serveur</option>
            <option value="CUISINIER">Cuisinier</option>
          </select>
        </div>
        <button type="submit" class="btn btn-primary">
          Créer un utilisateur
        </button>
      </form>

      <div class="users-list">
        <h2>Liste des utilisateurs</h2>
        <div v-if="users.length === 0" class="empty-state">
          Aucun utilisateur trouvé
        </div>
        <ul v-else>
          <li v-for="user in users" :key="user.id" class="user-item">
            <div class="user-info">
              <span>{{ user.username }}</span>
              <span>{{ user.email }}</span>
              <span class="badge">{{ user.role }}</span>
            </div>
            <div class="user-actions">
              <button @click="editUser(user)" class="btn btn-secondary">
                Modifier
              </button>
              <button @click="deleteUser(user.id)" class="btn btn-danger">
                Supprimer
              </button>
            </div>
          </li>
        </ul>
      </div>

      <div class="activities-section">
        <h2>Activités des employés</h2>
        <div v-if="activities.length === 0" class="empty-state">
          Aucune activité enregistrée
        </div>
        <ul v-else class="activities-list">
          <li v-for="activity in activities" :key="activity.id" class="activity-item">
            <span class="activity-user">{{ activity.username }}</span>
            <span class="activity-action">{{ activity.action }}</span>
            <span class="activity-time">{{ new Date(activity.timestamp).toLocaleString() }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import apiClient from '../../services/apiClient'
import NavBar from '../../components/NavBar.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const users = ref([])
const newUser = ref({ username: '', email: '', password: '', role: '' })
const activities = ref([])
const errorMessage = ref('')

/*const fetchUsers = async () => {
  try {
    const response = await apiClient.get('/users')
    users.value = response.data
  } catch (error) {
    console.error('Erreur lors de la récupération des utilisateurs:', error)
    if (error.response?.status === 403) {
      errorMessage.value = "Vous n'avez pas les droits nécessaires"
      // Optionally redirect to login if token is invalid
      if (!sessionStorage.getItem('jwtToken')) {
        router.push('/login')
      }
    }
  }
}*/

const createUser = async () => {
  try {
    const response = await apiClient.post('/admin/create-user', newUser.value)
    console.log('Utilisateur créé avec succès:', response.data)
    newUser.value = { username: '', email: '', password: '', role: '' }
    //await fetchUsers()
    //await fetchActivities()
    errorMessage.value = ''
  } catch (error) {
    console.error('Erreur lors de la création:', error)
    errorMessage.value = error.response?.data?.message || 'Erreur lors de la création'
  }
}

const editUser = (user) => {
  newUser.value = { ...user }
}

/*const deleteUser = async (id) => {
  try {
    await apiClient.delete(`/admin/users/${id}`)
    await fetchUsers()
    await fetchActivities()
    errorMessage.value = ''
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
    errorMessage.value = error.response?.data?.message || 'Erreur lors de la suppression'
  }
}*/

/*const fetchActivities = async () => {
  try {
    const response = await apiClient.get('/activities')
    activities.value = response.data
  } catch (error) {
    console.error('Erreur lors de la récupération des activités:', error)
    if (error.response?.status === 403) {
      errorMessage.value = "Vous n'avez pas les droits nécessaires"
    }
  }
}*/

// Initial data fetch with error handling
/*const initializeData = async () => {
  try {
    await Promise.all([fetchUsers(), fetchActivities()])
  } catch (error) {
    console.error('Erreur lors de l\'initialisation:', error)
  }
}

initializeData()*/
</script>

<style scoped>
.container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.error-message {
  background-color: #ff4444;
  color: white;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.user-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.users-list,
.activities-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}

.user-item,
.activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.user-info {
  display: flex;
  gap: 20px;
  align-items: center;
}

.badge {
  background: #42b983;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9em;
}

.user-actions {
  display: flex;
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 20px;
  color: #666;
  font-style: italic;
}

.activity-item {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 20px;
}

.activity-time {
  color: #666;
  font-size: 0.9em;
}
</style>
<template>
  <div class="register">
    <form @submit.prevent="registerUser">
      <input v-model="newUser.email" type="email" placeholder="Email" required />
      <input v-model="newUser.username" type="text" placeholder="Nom d'utilisateur" required />
      <input v-model="newUser.password" type="password" placeholder="Mot de passe" required />
      <input v-model="newUser.confirmPassword" type="password" placeholder="Confirmation de mot de passe" required />
      <button type="submit">Créer son compte</button>
    </form>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const newUser = ref({ email: '', username: '', password: '', confirmPassword: '' })
const errorMessage = ref('')
const router = useRouter()

const registerUser = async () => {
  if (newUser.value.password !== newUser.value.confirmPassword) {
    errorMessage.value = "Les mots de passe ne correspondent pas"
    return
  }

  try {
    const response = await axios.post('http://localhost:8080/api/auth/register', {
      email: newUser.value.email,
      username: newUser.value.username,
      password: newUser.value.password
    })
    newUser.value = { email: '', username: '', password: '', confirmPassword: '' }
    errorMessage.value = ''
    router.push('/login') // Redirige vers la page de connexion en cas de réussite
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = error.response.data
    }
  }
}
</script>

<style scoped>
.register {
  max-width: 450px;
  margin: 5rem auto;
  padding: 2rem;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

input {
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

input:focus {
  border-color: #42b983;
  outline: none;
  box-shadow: 0 0 0 2px rgba(66, 185, 131, 0.2);
}

button {
  background-color: #42b983;
  color: white;
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #369f6b;
}

.error {
  color: #ff4444;
  font-size: 0.95rem;
  text-align: center;
  margin-top: 1rem;
}
</style>

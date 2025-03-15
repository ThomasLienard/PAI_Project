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

const newUser = ref({ email: '', username: '', password: '', confirmPassword: '' })
const errorMessage = ref('')

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
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = 'Email déjà utilisé'
    }
  }
}
</script>

<style scoped>
.register {
  max-width: 400px;
  margin: 0 auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

input {
  padding: 0.5rem;
  font-size: 1rem;
}

button {
  padding: 0.5rem;
  font-size: 1rem;
  cursor: pointer;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>

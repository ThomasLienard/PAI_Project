<template>
  <div class="login">
    <form @submit.prevent="loginUser">
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Mot de passe" required />
      <button type="submit">Se connecter</button>
    </form>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const errorMessage = ref('')
const router = useRouter()

const loginUser = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      email: email.value,
      password: password.value
    })
    const role = response.data.role
    if (role === 'ADMIN') {
      router.push('/admin')
    } else if (role === 'CUISINIER') {
      router.push('/cuisinier')
    } else if (role === 'SERVEUR') {
      router.push('/serveur')
    } else {
      router.push('/user')
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message
    } else {
      errorMessage.value = 'Erreur lors de la connexion'
    }
  }
}
</script>

<style scoped>
.login {
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
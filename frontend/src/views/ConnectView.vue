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

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../services/authService';

const email = ref('');
const password = ref('');
const errorMessage = ref('');
const router = useRouter();

const loginUser = async () => {
  try {
    const response = await login(email.value, password.value);
    const { role, token } = response;
    console.log('Token:', token); // Afficher le token dans la console

    if (role === 'ADMIN') {
      router.push('/admin');
    } else if (role === 'CUISINIER') {
      router.push('/chef');
    } else if (role === 'SERVEUR') {
      router.push('/server');
    } else {
      router.push('/user');
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = 'Erreur lors de la connexion';
    }
  }
};
</script>

<style scoped>
.login {
  max-width: 400px;
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
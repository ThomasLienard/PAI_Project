<template>
  <div>
    <h1>Gestion des statistiques</h1>
    <div v-if="performances.length">
      <h2>Performances en temps réel</h2>
      <ul>
        <li v-for="performance in performances" :key="performance.id">
          {{ performance.metric }}: {{ performance.value }}
        </li>
      </ul>
    </div>
    <div v-if="trends.length">
      <h2>Tendances de consommation</h2>
      <ul>
        <li v-for="trend in trends" :key="trend.id">
          {{ trend.metric }}: {{ trend.value }}
        </li>
      </ul>
    </div>
    <div v-if="reviews.length">
      <h2>Avis clients et satisfaction</h2>
      <ul>
        <li v-for="review in reviews" :key="review.id">
          {{ review.customer }}: {{ review.comment }} - Note: {{ review.rating }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const performances = ref([])
const trends = ref([])
const reviews = ref([])

const fetchPerformances = async () => {
  const response = await axios.get('/api/statistics/performances')
  performances.value = response.data
}

const fetchTrends = async () => {
  const response = await axios.get('/api/statistics/trends')
  trends.value = response.data
}

const fetchReviews = async () => {
  const response = await axios.get('/api/statistics/reviews')
  reviews.value = response.data
}

fetchPerformances()
fetchTrends()
fetchReviews()
</script>

<style scoped>
div {
  margin-bottom: 2rem;
}

h2 {
  margin-bottom: 1rem;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  padding: 0.5rem 0;
  border-bottom: 1px solid #ccc;
}
</style>
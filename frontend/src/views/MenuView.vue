<template>
  <div>
    <NavBar />
    <MenuHeader />
    <div v-if="Object.keys(menu).length > 0">
      <CategoryList
        v-for="(dishes, category) in menu"
        :key="category"
        :category="category"
        :dishes="dishes"
      />
    </div>
    <div v-else>
      <p>Chargement du menu...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMenuGroupedByCategory } from '../services/apiClient';
import NavBar from '../components/NavBar.vue';
import MenuHeader from '../components/menu/MenuHeader.vue'
import CategoryList from '../components/menu/CategoryList.vue'

const menu = ref({})

const fetchMenu = async () => {
  menu.value = await getMenuGroupedByCategory()
}

onMounted(fetchMenu)
</script>

<style scoped>
/* Ajoutez des styles globaux si nécessaire */
</style>
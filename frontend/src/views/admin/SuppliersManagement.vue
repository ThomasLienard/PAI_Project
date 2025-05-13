<template>
  <div>
    <NavBar />
    <div class="container">
      <h1>Gestion des Fournisseurs</h1>

      <!-- Barre de recherche et filtres -->
      <div class="search-section">
        <div class="search-bar">
          <input 
            v-model="searchQuery" 
            type="text"
            placeholder="Rechercher un fournisseur..." 
          />
          <select v-model="filterCriteria">
            <option value="name">Nom</option>
            <option value="location">Localisation</option>
          </select>
        </div>
      </div>

      <!-- Message d'erreur -->
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

      <!-- Bouton pour afficher le formulaire -->
      <button class="btn-primary" @click="openForm" v-if="!showForm">
        Ajouter un fournisseur
      </button>

      <!-- Formulaire d'ajout/modification -->
      <form v-if="showForm" @submit.prevent="saveSupplier" class="supplier-form">
        <div class="form-grid">
          <div class="form-group">
            <label>Nom du fournisseur*</label>
            <input v-model="currentSupplier.name" required />
          </div>
          
          <div class="form-group">
            <label>Email*</label>
            <input v-model="currentSupplier.email" type="email" required />
          </div>

          <div class="form-group">
            <label>Localisation</label>
            <input v-model="currentSupplier.location" />
          </div>

          <div class="form-group">
            <label>Délai de paiement (jours)</label>
            <input v-model="currentSupplier.paymentTerms" type="number" />
          </div>

          <div class="form-group">
            <label>Frais de livraison (€)</label>
            <input v-model="currentSupplier.deliveryFee" type="number" />
          </div>
        </div>

        <button type="submit" class="btn-primary">
          {{ isEditing ? 'Modifier' : 'Ajouter' }} le fournisseur
        </button>
        <button type="button" class="btn-secondary" @click="closeForm">
          Annuler
        </button>
      </form>

      <!-- Liste des fournisseurs -->
      <div class="suppliers-list">
        <h2>Fournisseurs enregistrés</h2>
        <div class="cards-grid">
          <div v-for="supplier in filteredSuppliers" 
               :key="supplier.id" 
               class="supplier-card"
               :class="{ 'inactive': !supplier.active }">
            <div class="card-header">
              <h3>{{ supplier.name }}</h3>
              <div class="rating">
                <span>Note: {{ supplier.rating ? supplier.rating.toFixed(1) : 0 }}/5</span>
                <star-rating
                  v-model="supplier.rating"
                  @update:modelValue="rateSupplier(supplier)"
                ></star-rating>
              </div>
            </div>

            <div class="card-body">
              <p><strong>Email:</strong> {{ supplier.email }}</p>
              <p><strong>Localisation:</strong> {{ supplier.location }}</p>
              <p><strong>Délai de paiement:</strong> {{ supplier.paymentTerms }} jours</p>
              <p><strong>Frais de livraison:</strong> {{ supplier.deliveryFee }}€</p>
            </div>

            <div class="card-footer">
              <button @click="editSupplier(supplier)" class="btn-secondary">
                Modifier
              </button>
              <button v-if="supplier.active" @click="deactivateSupplier(supplier)" class="btn-warning">
                Désactiver
              </button>
              <button v-else @click="activateSupplier(supplier)" class="btn-success">
                Activer
              </button>
              <button @click="viewProducts(supplier)" class="btn-info">
                Catalogue
              </button>
              <button @click="viewOrders(supplier)" class="btn-info">
                Historique
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal Catalogue -->
      <div v-if="showCatalogModal" class="modal">
        <div class="modal-content">
          <h2>Catalogue de {{ selectedSupplier?.name }}</h2>
          <div class="products-grid">
            <!-- Liste des produits -->
          </div>
        </div>
      </div>

      <!-- Modal Historique -->
      <div v-if="showOrdersModal" class="modal">
        <div class="modal-content">
          <h2>Historique des commandes - {{ selectedSupplier?.name }}</h2>
          <table class="orders-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Produits</th>
                <th>Montant</th>
                <th>Statut</th>
              </tr>
            </thead>
            <tbody>
              <!-- Liste des commandes -->
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import NavBar from '../../components/NavBar.vue'
import StarRating from '../../components/StarRating.vue'
import apiClient from '@/services/apiClient'

// État
const suppliers = ref([])
const currentSupplier = ref({
  name: '',
  email: '',
  location: '',
  paymentTerms: 30,
  deliveryFee: 0,
  active: true,
  rating: 0
})

const searchQuery = ref('')
const filterCriteria = ref('name')
const isEditing = ref(false)
const showForm = ref(false)
const showCatalogModal = ref(false)
const showOrdersModal = ref(false)
const selectedSupplier = ref(null)
const errorMsg = ref('')

// Computed
const filteredSuppliers = computed(() => {
  let filtered = [...suppliers.value]
  
  // Filtrage
  if (searchQuery.value) {
    filtered = filtered.filter(supplier => {
      const searchLower = searchQuery.value.toLowerCase()
      switch (filterCriteria.value) {
        case 'name':
          return supplier.name && supplier.name.toLowerCase().includes(searchLower)
        case 'location':
          return supplier.location && supplier.location.toLowerCase().includes(searchLower)
      }
    })
  }

  // Tri par note décroissante (fiabilité)
  filtered.sort((a, b) => (b.rating || 0) - (a.rating || 0))

  return filtered
})

// Méthodes API
const fetchSuppliers = async () => {
  try {
    const res = await apiClient.get('/admin/suppliers/all')
    suppliers.value = res.data
  } catch (error) {
    console.error('Erreur lors du chargement des fournisseurs:', error)
  }
}

const createSupplier = async (supplier) => {
  await apiClient.post('/admin/suppliers', supplier)
}

const updateSupplier = async (supplier) => {
  await apiClient.put(`/admin/suppliers/${supplier.id}`, supplier)
}

const deactivateSupplier = async (supplier) => {
  await apiClient.patch(`/admin/suppliers/${supplier.id}/deactivate`)
  await fetchSuppliers()
}

const activateSupplier = async (supplier) => {
  await apiClient.patch(`/admin/suppliers/${supplier.id}/activate`)
  await fetchSuppliers()
}

const rateSupplier = async (supplier) => {
  try {
    await apiClient.patch(`/admin/suppliers/${supplier.id}/rate`,
      { rating: supplier.rating }
    )
    await fetchSuppliers()
  } catch (error) {
    errorMsg.value = "Erreur lors de l'enregistrement de la note"
    console.error(error)
  }
}

// Méthodes UI
const openForm = () => {
  resetForm()
  showForm.value = true
}

const closeForm = () => {
  resetForm()
  showForm.value = false
}

const saveSupplier = async () => {
  try {
    errorMsg.value = ''
    if (isEditing.value) {
      await updateSupplier(currentSupplier.value)
    } else {
      await createSupplier(currentSupplier.value)
    }
    resetForm()
    showForm.value = false
    await fetchSuppliers()
  } catch (error) {
    errorMsg.value = 'Erreur lors de la sauvegarde : ' + (error.response?.status === 403 ? 'Accès interdit. Êtes-vous connecté en tant qu\'admin ?' : error.message)
    console.error('Erreur lors de la sauvegarde:', error)
  }
}

const editSupplier = (supplier) => {
  currentSupplier.value = { ...supplier }
  isEditing.value = true
  showForm.value = true
}

const viewProducts = (supplier) => {
  selectedSupplier.value = supplier
  showCatalogModal.value = true
}

const viewOrders = (supplier) => {
  selectedSupplier.value = supplier
  showOrdersModal.value = true
}

const resetForm = () => {
  currentSupplier.value = {
    name: '',
    email: '',
    location: '',
    paymentTerms: 30,
    deliveryFee: 0,
    active: true,
    rating: 0
  }
  isEditing.value = false
}

// Chargement initial
onMounted(async () => {
  await fetchSuppliers()
})
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-section {
  margin-bottom: 30px;
}

.search-bar {
  display: flex;
  gap: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.supplier-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.supplier-card.inactive {
  opacity: 0.7;
  background-color: #f8f9fa;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.card-footer {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  max-width: 800px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-secondary {
  background-color: var(--secondary-color);
  color: white;
}

.btn-warning {
  background-color: var(--warning-color);
  color: black;
}

.btn-success {
  background-color: var(--success-color);
  color: white;
}

.btn-info {
  background-color: var(--info-color);
  color: white;
}

.error-msg {
  color: red;
  margin-bottom: 1em;
}

/* Responsive */
@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
  
  .card-header {
    flex-direction: column;
    text-align: center;
  }
  
  .card-footer {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>
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
            <option value="productType">Type de produits</option>
          </select>
          <select v-model="sortCriteria">
            <option value="reliability">Fiabilité</option>
            <option value="deliveryTime">Délai de livraison</option>
            <option value="terms">Conditions commerciales</option>
          </select>
        </div>
      </div>

      <!-- Formulaire d'ajout/modification -->
      <form @submit.prevent="saveSupplier" class="supplier-form">
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
            <label>Délai de paiement (jours)</label>
            <input v-model="currentSupplier.paymentTerms" type="number" />
          </div>

          <div class="form-group">
            <label>Minimum de commande (€)</label>
            <input v-model="currentSupplier.minimumOrder" type="number" />
          </div>

          <div class="form-group">
            <label>Frais de livraison (€)</label>
            <input v-model="currentSupplier.deliveryFee" type="number" />
          </div>
        </div>

        <button type="submit" class="btn-primary">
          {{ isEditing ? 'Modifier' : 'Ajouter' }} le fournisseur
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
                <span>Note: {{ supplier.rating }}/5</span>
                <star-rating v-model="supplier.rating" :read-only="true"></star-rating>
              </div>
            </div>

            <div class="card-body">
              <p><strong>Email:</strong> {{ supplier.email }}</p>
              <p><strong>Délai de paiement:</strong> {{ supplier.paymentTerms }} jours</p>
              <p><strong>Commande minimum:</strong> {{ supplier.minimumOrder }}€</p>
            </div>

            <div class="card-footer">
              <button @click="editSupplier(supplier)" class="btn-secondary">
                Modifier
              </button>
              <button @click="toggleSupplierStatus(supplier)" 
                      :class="supplier.active ? 'btn-warning' : 'btn-success'">
                {{ supplier.active ? 'Désactiver' : 'Activer' }}
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
import { ref, computed } from 'vue'
import NavBar from '../../components/NavBar.vue'
import StarRating from '../../components/StarRating.vue'

// État
const suppliers = ref([])
const currentSupplier = ref({
  name: '',
  email: '',
  paymentTerms: 30,
  minimumOrder: 0,
  deliveryFee: 0,
  active: true,
  rating: 0
})

const searchQuery = ref('')
const filterCriteria = ref('name')
const sortCriteria = ref('reliability')
const isEditing = ref(false)
const showCatalogModal = ref(false)
const showOrdersModal = ref(false)
const selectedSupplier = ref(null)

// Computed
const filteredSuppliers = computed(() => {
  let filtered = [...suppliers.value]
  
  // Filtrage
  if (searchQuery.value) {
    filtered = filtered.filter(supplier => {
      const searchLower = searchQuery.value.toLowerCase()
      switch (filterCriteria.value) {
        case 'name':
          return supplier.name.toLowerCase().includes(searchLower)
        case 'location':
          return supplier.location.toLowerCase().includes(searchLower)
        case 'productType':
          return supplier.productTypes.some(type => 
            type.toLowerCase().includes(searchLower)
          )
      }
    })
  }

  // Tri
  filtered.sort((a, b) => {
    switch (sortCriteria.value) {
      case 'reliability':
        return b.rating - a.rating
      case 'deliveryTime':
        return a.averageDeliveryTime - b.averageDeliveryTime
      case 'terms':
        return b.paymentTerms - a.paymentTerms
    }
  })

  return filtered
})

// Méthodes
const saveSupplier = async () => {
  try {
    if (isEditing.value) {
      await updateSupplier(currentSupplier.value)
    } else {
      await createSupplier(currentSupplier.value)
    }
    resetForm()
    await fetchSuppliers()
  } catch (error) {
    console.error('Erreur lors de la sauvegarde:', error)
  }
}

const editSupplier = (supplier) => {
  currentSupplier.value = { ...supplier }
  isEditing.value = true
}

const toggleSupplierStatus = async (supplier) => {
  try {
    await updateSupplier({
      ...supplier,
      active: !supplier.active
    })
    await fetchSuppliers()
  } catch (error) {
    console.error('Erreur lors du changement de statut:', error)
  }
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
    paymentTerms: 30,
    minimumOrder: 0,
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
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
          <h2>Catalogue de {{ catalogSupplier?.name }}</h2>
          <button class="btn-secondary" @click="closeCatalogModal">Fermer</button>
          <div v-if="productErrorMsg" class="error-msg">{{ productErrorMsg }}</div>
          <div>
            <h3>Ajouter / Modifier un produit</h3>
            <form @submit.prevent="saveProduct" class="product-form">
              <div>
                <label>Nom du produit : </label>
                <input v-model="productForm.name" placeholder="Nom du produit" required />
              </div>
              <div>
                <label>Prix (€) : </label>
                <input v-model.number="productForm.price" type="number" step="0.01" placeholder="Prix (€)" required />
              </div>
              <div>
                <label>Délai livraison (jours) : </label>
                <input v-model.number="productForm.usualDeliveryTime" type="number" placeholder="Délai livraison (jours)" required />
              </div>
              <button type="submit" class="btn-primary">{{ isEditingProduct ? 'Modifier' : 'Ajouter' }}</button>
              <button type="button" class="btn-secondary" @click="openAddProduct">Réinitialiser</button>
            </form>
          </div>
          <h3>Produits</h3>
          <table class="catalog-table">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Prix (€)</th>
                <th>Délai livraison (j)</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in catalogProducts" :key="product.id">
                <td>{{ product.name }}</td>
                <td>{{ product.price.toFixed(2) }}</td>
                <td>{{ product.usualDeliveryTime }}</td>
                <td>
                  <button class="btn-secondary" @click="openEditProduct(product)">Modifier</button>
                  <button class="btn-warning" @click="deleteProduct(product.id)">Supprimer</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Modal Historique -->
      <div v-if="showOrdersModal" class="modal">
        <div class="modal-content">
          <h2>Historique des commandes - {{ selectedSupplier?.name }}</h2>
          <button class="btn-secondary" @click="showOrdersModal = false">Fermer</button>
          <table class="orders-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Produits</th>
                <th>Montant</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="ordersHistory.length === 0">
                <td colspan="5" style="text-align:center;">Aucune commande</td>
              </tr>
              <tr v-for="order in ordersHistory" :key="order.id">
                <td>{{ new Date(order.orderDate).toLocaleDateString() }}</td>
                <td>
                  <ul>
                    <li v-for="line in order.lines" :key="line.id">
                      {{ line.quantity }} x {{ getProductName(line.productId) }}
                    </li>
                  </ul>
                </td>
                <td>{{ order.totalAmount.toFixed(2) }} €</td>
                <td>{{ order.status }}</td>
                <td>
                  <button class="btn-primary" @click="renewOrder(order.id)">Renouveler</button>
                  <button class="btn-primary" @click="modifyOrder(order.id)">Modifier</button>
                  <button class="btn-primary" @click="validateOrder(order.id)">Valider</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-if="showEditOrderModal" class="modal">
        <div class="modal-content">
        <h2>Modifier la commande</h2>
        <button class="btn-secondary" @click="cancelEditOrder">Annuler</button>
          <form @submit.prevent="saveEditedOrder">
            <table>
              <thead>
                <tr>
                  <th>Produit</th>
                  <th>Quantité reçue</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(line, idx) in editingOrderLines" :key="line.id">
                  <td>{{ getProductName(line.productId) }}</td>
                  <td>
                    <input type="number" v-model.number="line.quantity" min="0" />
                  </td>
                  <td>
                    <button
                      v-if="!line.delivered"
                      type="button"
                      class="btn-warning"
                      @click="removeOrderLine(idx)"
                    >
                      Supprimer
                    </button>
                    <span v-else style="color: #aaa;">Livré</span>
                  </td>
                </tr>
              </tbody>
            </table>
            <div style="margin-top: 1em;">
              <button type="submit" class="btn-primary">Valider la réception</button>
              <button type="button" class="btn-secondary" @click="cancelEditOrder">Revenir en arrière</button>
            </div>
          </form>
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
const catalogProducts = ref([])
const catalogSupplier = ref(null)
const productForm = ref({
  id: null,
  name: '',
  price: 0,
  usualDeliveryTime: 0
})
const isEditingProduct = ref(false)
const productErrorMsg = ref('')
const showOrdersModal = ref(false)
const selectedSupplier = ref(null)
const ordersHistory = ref([])
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

const fetchOrdersHistory = async (supplierId) => {
  try {
    const res = await apiClient.get(`/admin/supplier/orders/supplier/${supplierId}`)
    ordersHistory.value = res.data
  } catch (error) {
    errorMsg.value = "Erreur lors du chargement de l'historique des commandes"
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

const viewProducts = async (supplier) => {
  catalogSupplier.value = supplier
  showCatalogModal.value = true
  await fetchCatalog(supplier.id)
}

const fetchCatalog = async (supplierId) => {
  try {
    const res = await apiClient.get(`/admin/suppliers/${supplierId}/products`)
    catalogProducts.value = res.data
  } catch (error) {
    productErrorMsg.value = "Erreur lors du chargement du catalogue"
  }
}

const openAddProduct = () => {
  productForm.value = {
    id: null,
    name: '',
    price: 0,
    usualDeliveryTime: 0
  }
  isEditingProduct.value = false
}

const openEditProduct = (product) => {
  productForm.value = { ...product }
  isEditingProduct.value = true
}

const saveProduct = async () => {
  try {
    if (isEditingProduct.value) {
      await apiClient.put(`/admin/suppliers/${catalogSupplier.value.id}/products/${productForm.value.id}`, productForm.value)
    } else {
      await apiClient.post(`/admin/suppliers/${catalogSupplier.value.id}/products`, productForm.value)
    }
    await fetchCatalog(catalogSupplier.value.id)
    openAddProduct()
  } catch (error) {
    productErrorMsg.value = "Erreur lors de l'enregistrement du produit"
  }
}

const deleteProduct = async (productId) => {
  try {
    await apiClient.delete(`/admin/suppliers/${catalogSupplier.value.id}/products/${productId}`)
    await fetchCatalog(catalogSupplier.value.id)
  } catch (error) {
    productErrorMsg.value = "Erreur lors de la suppression du produit"
  }
}

const closeCatalogModal = () => {
  showCatalogModal.value = false
  catalogProducts.value = []
  catalogSupplier.value = null
  productErrorMsg.value = ''
}

const viewOrders = async (supplier) => {
  selectedSupplier.value = supplier
  await fetchOrdersHistory(supplier.id)
  showOrdersModal.value = true
}

const getProductName = (productId) => {
  const product = catalogProducts.value.find(p => p.id === productId)
  return product ? product.name : `Produit #${productId}`
}

const renewOrder = async (previousOrderId) => {
  try {
    await apiClient.post(`/admin/supplier/orders/${previousOrderId}/renew`)
    await fetchOrdersHistory(selectedSupplier.value.id)
  } catch (error) {
    errorMsg.value = "Erreur lors du renouvellement de la commande"
  }
}

const validateOrder = async (orderId) => {
  try {
    await apiClient.put(`/admin/supplier/orders/${orderId}/validate`)
    await fetchOrdersHistory(selectedSupplier.value.id)
  } catch (error) {
    errorMsg.value = "Erreur lors de la validation de la commande"
  }
}

const editingOrder = ref(null); 
const editingOrderLines = ref([]); 
const showEditOrderModal = ref(false);

const modifyOrder = (orderId) => {
  const order = ordersHistory.value.find(o => o.id === orderId);
  if (!order) return;
  editingOrder.value = { ...order };
  editingOrderLines.value = order.lines.map(line => ({ ...line }));
  showEditOrderModal.value = true;
};

const cancelEditOrder = () => {
  showEditOrderModal.value = false;
  editingOrder.value = null;
  editingOrderLines.value = [];
};

const removeOrderLine = (idx) => {
  editingOrderLines.value.splice(idx, 1);
};

const saveEditedOrder = async () => {
  try {
    await apiClient.put(`/admin/supplier/orders/${editingOrder.value.id}/update-lines`, {
      lines: editingOrderLines.value
    });
    await fetchOrdersHistory(selectedSupplier.value.id);
    showEditOrderModal.value = false;
    editingOrder.value = null;
    editingOrderLines.value = [];
  } catch (error) {
    errorMsg.value = "Erreur lors de la modification de la commande";
  }
};

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

.catalog-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.catalog-table th, .catalog-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.catalog-table th {
  background-color: #f4f4f4;
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
  background-color:  beige;
  color: black;
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
<template>
  <div class="special-instructions-overlay">
    <div class="special-instructions-modal">
      <div class="modal-header">
        <h3>Instructions spéciales</h3>
        <button class="close-btn" @click="close">
          <i class="fas fa-times"></i>
        </button>
      </div>
      
      <div class="modal-body">
        <div class="item-info">
          <span class="item-name">{{ item.name }}</span>
          <span class="item-quantity">x{{ item.quantity }}</span>
        </div>
        
        <div class="input-group">
          <label for="special-instructions">Précisez les instructions spéciales :</label>
          <textarea 
            id="special-instructions" 
            v-model="instructions"
            placeholder="Ex: Sans oignons, bien cuit, sauce à part..."
            rows="3"
            maxlength="255"
          ></textarea>
          <div class="character-count" :class="{ 'near-limit': isNearCharLimit }">
            {{ instructions.length }}/255
          </div>
        </div>
        
        <div class="quick-options">
          <span class="quick-options-title">Options rapides:</span>
          <div class="quick-options-buttons">
            <button 
              v-for="option in quickOptions" 
              :key="option"
              @click="addQuickOption(option)"
              class="quick-option-btn"
            >
              {{ option }}
            </button>
          </div>
        </div>
      </div>
      
      <div class="modal-footer">
        <button class="cancel-btn" @click="close">Annuler</button>
        <button class="save-btn" @click="save">Enregistrer</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';

export default {
  name: 'SpecialInstructionsModal',
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  emits: ['save', 'close'],
  setup(props, { emit }) {
    const instructions = ref('');
    
    // Options rapides prédéfinies
    const quickOptions = [
      'Sans sel',
      'Bien cuit',
      'À point',
      'Saignant',
      'Sans sauce',
      'Sauce à part',
      'Sans oignons',
      'Sans gluten'
    ];
    
    // Vérifier si on approche de la limite de caractères
    const isNearCharLimit = computed(() => {
      return instructions.value.length > 230;
    });
    
    // Ajouter une option rapide au texte
    const addQuickOption = (option) => {
      if (instructions.value) {
        if (instructions.value.endsWith('.') || 
            instructions.value.endsWith(',') || 
            instructions.value.endsWith(' ')) {
          instructions.value += ' ' + option;
        } else {
          instructions.value += ', ' + option;
        }
      } else {
        instructions.value = option;
      }
    };
    
    // Fermer le modal
    const close = () => {
      emit('close');
    };
    
    // Sauvegarder les instructions
    const save = () => {
      emit('save', props.item.id, instructions.value.trim());
    };
    
    // Initialiser avec les instructions existantes si présentes
    onMounted(() => {
      if (props.item.specialInstructions) {
        instructions.value = props.item.specialInstructions;
      }
    });
    
    return {
      instructions,
      quickOptions,
      isNearCharLimit,
      addQuickOption,
      close,
      save
    };
  }
}
</script>

<style scoped>
.special-instructions-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.special-instructions-modal {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #ddd;
}

.modal-header h3 {
  margin: 0;
  color: #2196f3;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 1rem;
  overflow-y: auto;
}

.item-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
}

.item-name {
  font-weight: bold;
}

.item-quantity {
  font-weight: bold;
  color: #2196f3;
}

.input-group {
  margin-bottom: 1.5rem;
  position: relative;
}

.input-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
}

.input-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  font-family: inherit;
  font-size: 1rem;
}

.input-group textarea:focus {
  outline: none;
  border-color: #2196f3;
  box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.2);
}

.character-count {
  position: absolute;
  bottom: -20px;
  right: 0;
  font-size: 0.8rem;
  color: #999;
}

.character-count.near-limit {
  color: #f44336;
  font-weight: bold;
}

.quick-options {
  margin-top: 1.5rem;
}

.quick-options-title {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
}

.quick-options-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.quick-option-btn {
  padding: 0.5rem 0.75rem;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.2s;
}

.quick-option-btn:hover {
  background-color: #e0e0e0;
}

.modal-footer {
  padding: 1rem;
  border-top: 1px solid #ddd;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.cancel-btn, .save-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  border: none;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #333;
}

.save-btn {
  background-color: #2196f3;
  color: white;
}

.save-btn:hover {
  background-color: #1976d2;
}
</style>
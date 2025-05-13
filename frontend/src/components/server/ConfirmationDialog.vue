<template>
  <div class="confirmation-overlay">
    <div class="confirmation-dialog">
      <div class="dialog-icon" :class="{ 'warning': isWarning }">
        <i :class="isWarning ? 'fas fa-exclamation-triangle' : 'fas fa-question-circle'"></i>
      </div>
      
      <div class="dialog-content">
        <h3>{{ title || 'Confirmation' }}</h3>
        <p>{{ message }}</p>
      </div>
      
      <div class="dialog-actions">
        <button 
          class="cancel-btn" 
          @click="$emit('cancel')"
        >
          {{ cancelText || 'Annuler' }}
        </button>
        <button 
          class="confirm-btn" 
          :class="{ 'warning-btn': isWarning }"
          @click="$emit('confirm')"
        >
          {{ confirmText || 'Confirmer' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmationDialog',
  props: {
    title: {
      type: String,
      default: ''
    },
    message: {
      type: String,
      required: true
    },
    confirmText: {
      type: String,
      default: ''
    },
    cancelText: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'confirm',
      validator: (value) => ['confirm', 'warning'].includes(value)
    }
  },
  emits: ['confirm', 'cancel'],
  setup(props) {
    const isWarning = props.type === 'warning';
    
    return {
      isWarning
    };
  }
}
</script>

<style scoped>
.confirmation-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1100;
}

.confirmation-dialog {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 450px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  padding: 1.5rem;
  text-align: center;
}

.dialog-icon {
  font-size: 3rem;
  color: #2196f3;
  margin-bottom: 1rem;
}

.dialog-icon.warning {
  color: #f44336;
}

.dialog-content h3 {
  margin-top: 0;
  margin-bottom: 0.75rem;
  font-size: 1.3rem;
}

.dialog-content p {
  color: #666;
  margin-bottom: 1.5rem;
}

.dialog-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.cancel-btn, .confirm-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  min-width: 100px;
  border: none;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #333;
}

.confirm-btn {
  background-color: #2196f3;
  color: white;
}

.confirm-btn.warning-btn {
  background-color: #f44336;
}

.confirm-btn:hover {
  opacity: 0.9;
}
</style>
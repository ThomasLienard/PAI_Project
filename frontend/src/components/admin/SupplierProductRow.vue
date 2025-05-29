<template>
  <tr>
    <td>{{ product.name }}</td>
    <td>{{ formatCurrency(product.unitPrice) }}</td>
    <td>
      <input
        type="number"
        min="0"
        :value="quantity"
        @input="$emit('update-quantity', parseInt($event.target.value) || 0)"
        style="width: 70px;"
      />
    </td>
    <td>{{ formatCurrency(subTotal) }}</td>
  </tr>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  product: {
    type: Object,
    required: true
  },
  quantity: {
    type: Number,
    required: true,
    default: 0
  }
});

defineEmits(['update-quantity']);

const subTotal = computed(() => {
  return (props.product.unitPrice || 0) * props.quantity;
});

const formatCurrency = (value) => {
  if (typeof value !== 'number') return 'N/A';
  return value.toFixed(2) + ' €';
};
</script>

<style scoped>
input[type="number"] {
  padding: 5px;
  border-radius: 3px;
  border: 1px solid #ccc;
}
</style>
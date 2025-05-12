<template>
  <span :class="['status-badge', status.toLowerCase()]">
    <template v-if="status === 'en_attente'">⏳ En attente</template>
    <template v-else-if="status === 'en_preparation'">🕒 En préparation</template>
    <template v-else-if="status === 'prete'">✅ Prête</template>
    <template v-else-if="status === 'servie'">🍽️ Servie</template>
    <template v-else>{{ status }}</template>
  </span>
</template>

<script setup>
defineProps({ status: String });
</script>

<style scoped>
.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.95em;
  color: #fff;
  display: inline-block;
  min-width: 80px;
  text-align: center;
}
.status-badge.en_attente { background: #6c757d; }
.status-badge.en_preparation { background: #ffc107; color: #333; }
.status-badge.prete { background: #28a745; }
.status-badge.servie { background: #007bff; }
</style>
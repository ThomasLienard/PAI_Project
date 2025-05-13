<template>
  <div class="filters">
    <div class="search-bar">
      <i class="fas fa-search search-icon"></i>
      <input 
        :value="searchQuery"
        @input="$emit('update:searchQuery', $event.target.value)"
        placeholder="Rechercher par nom de client..."
        type="text"
      />
    </div>
    
    <div class="filter-options">
      <select 
        :value="timeSlotFilter" 
        @change="$emit('update:timeSlotFilter', $event.target.value)"
      >
        <option value="">Tous les créneaux</option>
        <option value="midi">Déjeuner</option>
        <option value="soir">Dîner</option>
      </select>
      
      <select 
        :value="groupSizeFilter" 
        @change="$emit('update:groupSizeFilter', $event.target.value)"
      >
        <option value="">Toutes tailles de groupe</option>
        <option value="1-2">1-2 personnes</option>
        <option value="3-4">3-4 personnes</option>
        <option value="5+">5+ personnes</option>
      </select>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReservationFilters',
  props: {
    searchQuery: {
      type: String,
      default: ''
    },
    timeSlotFilter: {
      type: String,
      default: ''
    },
    groupSizeFilter: {
      type: String,
      default: ''
    }
  },
  emits: ['update:searchQuery', 'update:timeSlotFilter', 'update:groupSizeFilter']
}
</script>
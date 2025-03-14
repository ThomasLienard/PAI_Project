<script setup lang="ts">
</script>

<template>
  <main>
    Tables disponibles:


    <form action="">

        <div>
            Voulez vous recevoir un message de confirmation par mail ?
            <input type="radio" name="confirmation_mail_oui" value="oui">Oui
            <input type="radio" name="confirmation_mail_non" value="non">Non
        </div>
        <hr>
        <div>
            Voulez vous recevoir un message de confirmation par sms ?
            <input type="radio" name="confirmation_sms_oui" value="oui">Oui
            <input type="radio" name="confirmation_sms_non" value="non">Non
        </div>
        <hr>
        <div>
            Voulez vous recevoir un message rappel ?
            <input type="radio" name="rappel_oui" value="oui">Oui
            <input type="radio" name="rappel_non" value="non">Non
        </div>
        <hr>
        <button type="submit">Réserver ma table</button>
    </form>

  </main>
</template>

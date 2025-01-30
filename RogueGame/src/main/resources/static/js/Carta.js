// Funzione per validare la scadenza della carta di credito
function validaScadenza(scadenza) {
    const regex = /^(0[1-9]|1[0-2])\/\d{2}$/; // Formato MM/YY
    if (!regex.test(scadenza)) {
        return false; // Formato non valido
    }

    // Ottieni il mese e l'anno dalla scadenza
    const [mese, anno] = scadenza.split('/');
    const scadenzaDate = new Date(2000 + parseInt(anno), parseInt(mese) - 1); // Anno 20YY
    const oggi = new Date();

    // Verifica se la scadenza è successiva alla data corrente
    return scadenzaDate > oggi;
}

// Aggiungi un listener al form di aggiunta
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('aggiungi-form')?.addEventListener('submit', function (event) {
        const scadenzaInput = document.getElementById('scadenza-aggiungi');
        if (!validaScadenza(scadenzaInput.value)) {
            alert('La data di scadenza non è valida. Deve essere successiva alla data corrente.');
            event.preventDefault(); // Blocca l'invio del form
        }
    });

    // Aggiungi un listener al form di modifica
    document.getElementById('modifica-form')?.addEventListener('submit', function (event) {
        const scadenzaInput = document.getElementById('scadenza-modifica');
        if (!validaScadenza(scadenzaInput.value)) {
            alert('La data di scadenza non è valida. Deve essere successiva alla data corrente.');
            event.preventDefault(); // Blocca l'invio del form
        }
    });
});

let campoModifica = ''; // Memorizza il campo che si vuole modificare

// Funzione per aprire la modale
function apriModale(campo) {
    campoModifica = campo;
    document.getElementById('modale').style.display = 'flex';
    document.getElementById('modale-titolo').textContent = `Modifica ${campo.charAt(0).toUpperCase() + campo.slice(1)}`;
}

// Funzione per chiudere la modale
function chiudiModale() {
    document.getElementById('modale').style.display = 'none';
}

// Funzione per inviare la modifica
function inviaModifica(event) {
    event.preventDefault(); // Prevenire il comportamento di invio del form

    const nuovoValore = document.getElementById('nuovoValore').value;

    fetch(`/utenti/modifica`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ campo: campoModifica, valore: nuovoValore })
    }).then(response => {
        if (response.ok) {
            alert(`${campoModifica} aggiornato con successo!`);
            location.reload(); // Ricarica la pagina per aggiornare i dati
        } else {
            alert('Errore durante l\'aggiornamento. Riprova.');
        }
        chiudiModale(); // Chiudi la modale
    });
}

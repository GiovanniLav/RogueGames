function inviaModifica(event) {
    event.preventDefault(); // Evita il refresh della pagina

    const nuovoValore = document.getElementById('nuovoValore').value;

    fetch(`/utenti/modifica`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ campo: campoModifica, valore: nuovoValore })
    })
        .then(response => response.json().then(data => ({ status: response.status, body: data }))) // Legge sempre JSON
        .then(result => {
            if (result.status === 200) {
                alert(result.body.messaggio); // Mostra il messaggio di successo
                location.reload(); // Ricarica la pagina per aggiornare i dati
            } else {
                alert(result.body.errore); // Mostra il messaggio di errore personalizzato
            }
            chiudiModale(); // Chiude la modale in ogni caso
        })
        .catch(() => {
            alert('Errore di connessione. Riprova più tardi.');
            chiudiModale();
        });
}


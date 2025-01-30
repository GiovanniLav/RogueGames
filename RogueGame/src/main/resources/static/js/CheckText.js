function controllaEta(eta) {
	if (isNaN(eta.value) || parseInt(eta.value) < 0 || parseInt(eta.value) > 150) {
		alert('L\' età inserita non è corretta');
		eta.value = "";
		eta.focus();
	}
}

function controllaQuantita(quantita) {
	if (isNaN(quantita.value) || parseInt(quantita.value) < 0) {
		alert('La quantità inserita non è corretta');
		quantita.value = "";
		quantita.focus();
	}
}

function controllaPrezzo(prezzo) {
	if (isNaN(prezzo.value) || prezzo.value < 0) {
		alert('Il prezzo inserito non è corretto');
		prezzo.value = "";
		prezzo.focus();
	}
}


	// Ottieni gli elementi
	var modal = document.getElementById("addProductModal");
	var btn = document.getElementById("addProductBtn");
	var closeBtn = document.getElementById("closeModalBtn");

	// Aggiungi l'evento per aprire la modale quando si clicca su "Aggiungi prodotto"
	btn.onclick = function() {
	modal.style.display = "block";
	document.getElementById("modalTitle").textContent = "Aggiungi Nuovo Prodotto";  // Resetta il titolo
	document.getElementById("submitButton").textContent = "Aggiungi Prodotto";  // Resetta il testo del bottone
	document.getElementById("productForm").action = "/utenti/prodotti/aggiungi";  // Resetta l'azione del form
}

	// Aggiungi l'evento per chiudere la modale quando si clicca sulla "X"
	closeBtn.onclick = function() {
	modal.style.display = "none";
}

	// Chiudi la modale se l'utente clicca al di fuori della finestra modale
	window.onclick = function(event) {
	if (event.target === modal) {
	modal.style.display = "none";
}
}

	function openEditModal(prodottoNome) {
	console.log("Apertura modale per prodotto:", prodottoNome);  // Aggiungi un log per vedere se la funzione viene eseguita
	// Esegui una richiesta GET per ottenere i dati del prodotto
	fetch('/utenti/prodotti/' + prodottoNome)
	.then(response => response.json())
	.then(data => {
	console.log("Dati prodotto recuperati:", data);  // Verifica che i dati siano corretti
	// Popola il form della modale con i dati del prodotto
	document.getElementById("nome").value = data.nome;
	document.getElementById("immagine").value = data.immagine;
	document.getElementById("video").value = data.video;
	document.getElementById("descrizione").value = data.descrizione;
	document.getElementById("prezzo").value = data.prezzo;
	document.getElementById("casaProd").value = data.casaProd;
	document.getElementById("piattaforma").value = data.piattaforma;
	document.getElementById("genere").value = data.genere;
	document.getElementById("tipo").value = data.tipo;
	document.getElementById("dataRilascio").value = data.dataRilascio;
	document.getElementById("quantita").value = data.quantita;

	// Cambia il titolo della modale per indicare che stiamo modificando
	document.getElementById("modalTitle").textContent = "Modifica Prodotto";

	// Cambia l'azione del form per "modifica"
	var form = document.querySelector("#productForm");
	form.action = "/utenti/prodotti/modifica/" + prodottoNome;

	// Cambia il testo del bottone per "Modifica Prodotto"
	document.getElementById("submitButton").textContent = "Modifica Prodotto";

	// Mostra la modale
	modal.style.display = "block";
})
	.catch(error => {
	console.log('Errore nel recupero dei dati del prodotto:', error);
});
}


	document.addEventListener("DOMContentLoaded", function() {
	const deleteButtons = document.querySelectorAll(".delete-btn");

	deleteButtons.forEach(button => {
	button.addEventListener("click", function(event) {
	const prodottoNome = this.getAttribute("data-nome");

	// Chiedi conferma prima di eliminare
	if (confirm("Sei sicuro di voler eliminare " + prodottoNome + "?")) {
	// Invia la richiesta POST per eliminare il prodotto
	fetch('/utenti/prodotti/elimina/' + prodottoNome, {
	method: 'POST',
})
	.then(response => {
	if (response.ok) {
	// Se la risposta è positiva, ricarica la pagina per mostrare i cambiamenti
	location.reload();
} else {
	alert("Errore nell'eliminazione del prodotto.");
}
})
	.catch(error => {
	console.log('Errore nel recupero della richiesta di eliminazione:', error);
	alert("Errore nell'eliminazione del prodotto.");
});
}
});
});
});

	function provaAlert(message) {
	const alertDiv = document.createElement("messageDiv");
	alertDiv.textContent = message;
	alertDiv.setAttribute("style", `
                background-color: #222;
                color: #ff9900;
                padding: 10px;
                border-radius: 5px;
                position: fixed;
                bottom: 20px;
                right: 20px;
                z-index: 1000;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            `);

	document.body.appendChild(alertDiv);

	// Rimuove l'alert dopo 3 secondi
	setTimeout(() => {
	alertDiv.remove();
}, 3000);
}



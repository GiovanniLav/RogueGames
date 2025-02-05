document.addEventListener("DOMContentLoaded", function() {
    var modal = document.getElementById("addProductModal");
    var btn = document.getElementById("addProductBtn");
    var closeBtn = document.getElementById("closeModalBtn");
    var form = document.getElementById("productForm");

    // Gestione dell'apertura del modal per aggiungere un prodotto
    btn.onclick = function() {
        form.reset(); // Resetta tutti i campi
        modal.style.display = "block";

        document.getElementById("modalTitle").textContent = "Aggiungi Nuovo Prodotto";
        document.getElementById("submitButton").textContent = "Aggiungi Prodotto";
        var v="";
        document.getElementById('video').addEventListener('change', function(event) {
            const vid=event.target.files[0];
            if(vid)
            {
                console.log (vid.name)
                v=vid.name;
            }else{
                console.log (vid.name)
                v="";
            }
        })
        document.getElementById('immagine').addEventListener('change', function(event) {
            const file = event.target.files[0];

            if (file) {

                console.log(file.name);
                console.log(v);
                $("#productForm").submit(function(event) {
                    event.preventDefault();
                    var nomeProdotto = $("#nome").val();
                    var nomeImmagine = file.name
                    var nomeVideo = v;

                    var formData = new FormData();
                    formData.append("nome", nomeProdotto);
                    formData.append("immagine", nomeImmagine);
                    formData.append("video", nomeVideo);
                    formData.append("descrizione", $("#descrizione").val());
                    formData.append("prezzo", $("#prezzo").val());
                    formData.append("quantita", $("#quantita").val());
                    formData.append("casaProd", $("#casaProd").val());
                    formData.append("piattaforma", $("#piattaforma").val());
                    formData.append("genere", $("#genere").val());
                    formData.append("tipo", $("#tipo").val());
                    formData.append("dataRilascio", $("#dataRilascio").val());
                    for (var pair of formData.entries()) {
                        console.log(pair[0] + ": " + pair[1]);
                    }            $.ajax({
                        url: "/utenti/prodotti/aggiungi",
                        type: "POST",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            console.log("Prodotto aggiunto:", response);
                            $("#addProductModal").hide();
                            provaAlert("Prodotto aggiunto correttamente")
                            setTimeout(() => {
                                location.reload(); // Ricarica la pagina dopo 3 secondi
                            }, 2000);
                        },
                        error: function(xhr,error) {
                            console.error("Errore:", error);
                            if (xhr.status === 511) {
                                provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                                setTimeout(() => {
                                    window.location.href = "/utenti/login";
                                }, 2000);
                            } else if (xhr.status === 401) {
                                provaAlert("Non sei autorizzato a stare in questa pagina");
                                setTimeout(() => {
                                    window.location.href = "/utenti/home";
                                }, 2000);
                            } if (xhr.status === 400) {
                                provaAlert("il prodotto è esistente");
                            } else {
                                provaAlert("Errore durante la richiesta." + error);
                            }
                        }
                    });
                });
            } else {
                provaAlert("Devi selezionare un immagine")
            }
        });
        // Rendi il campo 'nome' modificabile quando si aggiunge un prodotto
        document.getElementById("nome").removeAttribute("readonly");  // Rendi il nome modificabile

        // Azzera le etichette per immagine e video, rimuovi i valori preimpostati
        document.getElementById("immagine-label").textContent = "Immagine (Vuoto)";
        document.getElementById("video-label").textContent = "Video (Vuoto)";
        document.getElementById("hiddenImage").value = "";
        document.getElementById("hiddenVideo").value = "";

    };

    // Gestione della chiusura del modal
    closeBtn.onclick = function() {
        modal.style.display = "none";
    };

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }

    };

    // Funzione per aprire il modal in modalità modifica
    window.openEditModal = function(prodottoNome) {
        fetch('/utenti/prodotti/' + prodottoNome)
            .then(response => response.json())
            .then(data => {
                // Precompila i campi con i dati del prodotto
                document.getElementById("nome").value = data.nome;
                document.getElementById("descrizione").value = data.descrizione;
                document.getElementById("prezzo").value = data.prezzo;
                document.getElementById("casaProd").value = data.casaProd;
                document.getElementById("piattaforma").value = data.piattaforma;
                document.getElementById("genere").value = data.genere;
                document.getElementById("tipo").value = data.tipo;
                document.getElementById("dataRilascio").value = data.dataRilascio;
                document.getElementById("quantita").value = data.quantita;

                // Mostra il nome del file immagine e video esistente
                if (data.immagine) {
                    document.getElementById("immagine-label").textContent = "Immagine attuale: " + data.immagine;
                    document.getElementById("hiddenImage").value = data.immagine;
                }
                if (data.video) {
                    document.getElementById("video-label").textContent = "Video attuale: " + data.video;
                    document.getElementById("hiddenVideo").value = data.video;
                }

                // Imposta il titolo e il testo del pulsante per la modifica
                document.getElementById("modalTitle").textContent = "Modifica Prodotto";
                document.getElementById("productForm").action = "/utenti/prodotti/modifica/" + prodottoNome;
                document.getElementById("submitButton").textContent = "Modifica Prodotto";

                // Rendi il campo 'nome' non modificabile quando si modifica un prodotto
                document.getElementById("nome").setAttribute("readonly", true);  // Disabilita la modifica del nome

                modal.style.display = "block";
            })
            .catch(error => {
                console.log('Errore nel recupero dei dati del prodotto:', error);
            });
    };

    // Gestione del modulo di eliminazione
    const deleteButtons = document.querySelectorAll(".delete-btn");
    deleteButtons.forEach(button => {
        button.addEventListener("click", function() {
            const prodottoNome = this.getAttribute("data-nome");
            if (confirm("Sei sicuro di voler eliminare " + prodottoNome + "?")) {
                fetch('/utenti/prodotti/elimina/' + prodottoNome, { method: 'POST' })
                    .then(response => {
                        if (response.ok) {
                            location.reload();
                        } else {
                            alert("Errore nell'eliminazione del prodotto.");
                        }
                    })
                    .catch(error => {
                        console.log('Errore nell eliminazione del prodotto:', error);
                        alert("Errore nell'eliminazione del prodotto.");
                    });
            }
        });
    });

    // Validazione del modulo prima dell'invio
    form.addEventListener("submit", function(event) {
        var prezzo = parseFloat(document.getElementById("prezzo").value);
        var quantita = parseInt(document.getElementById("quantita").value);
        var immagine = document.getElementById("immagine").files[0];
        var video = document.getElementById("video").files[0];

        // Controllo prezzo e quantità
        if (prezzo < 0) {
            alert("Il prezzo deve essere maggiore o uguale a 0.");
            event.preventDefault();
            return;
        }
        if (quantita < 0) {
            alert("La quantità deve essere maggiore o uguale a 0.");
            event.preventDefault();
            return;
        }

        // Controllo immagine
        if (immagine) {
            var imgExt = immagine.name.split('.').pop().toLowerCase();
            if (!["jpg", "jpeg", "png"].includes(imgExt)) {
                alert("Formato immagine non valido! Accettati: .jpg, .jpeg, .png");
                event.preventDefault();
                return;
            }
        }

        // Controllo video
        if (video) {
            var vidExt = video.name.split('.').pop().toLowerCase();
            if (vidExt !== "mp4") {
                alert("Formato video non valido! Accettato: .mp4");
                event.preventDefault();
            }
        }
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



$(document).ready(function() {
    $('.processa-ordine').off('click').on('click', function (e) {
        e.preventDefault();

        var form = $(this).closest('form');
        const $button = $(this);
        var totale = $(this).data('totale'); // Assumiamo che il nome del prodotto sia memorizzato in un attributo data
        var url = "/aggiungiOrdine"; // Sostituisce {nome} con il valore reale

        $.ajax({
            url: url,
            type: "POST",
            data: { totale: totale },
            // ... altri parametri
            success: function (response) {
                Swal.fire({
                    title: 'Ordine Confermato!',
                    text: response,
                    icon: 'success'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = "/catalogo/prodotti";
                    }
                });
            },
            error: function (jqXHR, textStatus, errorThrown, xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Si è verificato un errore durante l\'elaborazione dell\'ordine.'
                });
                if (xhr.status === 400) {
                    provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                    setTimeout(() => {
                        window.location.href = "/utenti/home"; // Ricarica la pagina dopo 3 secondi
                    }, 2000);
                }else if (xhr.status === 401) {
                    provaAlert("Errore: Il gestore non può acquistare. Registrati con l'account cliente");
                    provaAlert("Errore: Devi loggarti per poter aggiungere prodotti al carrello");
                    setTimeout(() => {
                        window.location.href = "/utenti/login"; // Ricarica la pagina dopo 3 secondi
                    }, 2000);
                }
            }
        })
    })
})

$(document).ready(function () {
    $('.aggiungi-carta').on('click', function (e) {
        e.preventDefault();

        // Regex e controlli (come nel codice precedente)
        const cifRegex = /^\d{16}$/;
        const cvvRegex = /^\d{3}$/;
        const scadenzaRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;

        const cif = $('#cif').val();
        const cvv = $('#cvv').val();
        const scadenza = $('#scadenza').val();

        // Validazione (come nel codice precedente)
        if (!cifRegex.test(cif)) {
            $('#cifError').text('Il numero della carta deve contenere 16 cifre numeriche.');
            return;
        }
        if (!cvvRegex.test(cvv)) {
            $('#cvvError').text('Il CVV deve contenere 3 cifre numeriche.');
            return;
        }
        if (!scadenzaRegex.test(scadenza)) {
            $('#scadenzaError').text('Formato scadenza non valido. Usa MM/AA.');
            return;
        }

        // Controllo scadenza (come nel codice precedente)
        const oggi = new Date();
        const [meseScadenza, annoScadenza] = scadenza.split('/');
        const annoCorrente = oggi.getFullYear() % 100;
        const meseCorrente = oggi.getMonth() + 1;

        if (
            parseInt(annoScadenza) < annoCorrente ||
            (parseInt(annoScadenza) === annoCorrente && parseInt(meseScadenza) < meseCorrente)
        ) {
            $('#scadenzaError').text('La carta è scaduta.');
            return;
        }

        // AJAX per aggiungere la carta
        $.ajax({
            url: "/carte/salvaCartaOrdine",
            type: "POST",
            data: { cif: cif, scadenza: scadenza, cvv: cvv },
            success: function (response) {
                // Aggiungi la nuova carta al campo di selezione
                const nuovaCarta = `<option value="${cif}">${cif} ${scadenza}</option>`;
                $('#cartacredito').append(nuovaCarta);

                // Mostra un messaggio di successo
                Swal.fire('Successo', 'Carta aggiunta correttamente!', 'success');

                // Svuota i campi del form
                $('#myFormCarta')[0].reset(); // Usa .reset() per svuotare il form

                // Chiudi il form
                closeForm('myFormCarta');
            },
            error: function (xhr) {
                Swal.fire('Errore', xhr.responseJSON?.message || 'Errore durante l\'aggiunta della carta.', 'error');
            }
        });
    });
});

$(document).ready(function () {
    $('.aggiungi-indirizzo').on('click', function (e) {
        e.preventDefault();

        // Regex e controlli (come nel codice precedente)
        const capRegex = /^\d{5}$/;
        const provinciaRegex = /^[A-Z]{2}$/;
        const cittaRegex = /^[a-zA-Z\s]{2,45}$/;
        const civicoRegex = /^[a-zA-Z0-9\s]{1,10}$/;
        const viaRegex = /^[a-zA-Z\s]{2,45}$/;

        const provincia = $('#provincia').val();
        const cap = $('#cap').val();
        const via = $('#via').val();
        const civico = $('#civico').val();
        const citta = $('#citta').val();

        // Validazione (come nel codice precedente)
        if (!provinciaRegex.test(provincia)) {
            $('#provinciaError').text('La provincia deve contenere 2 lettere maiuscole.');
            return;
        }
        if (!capRegex.test(cap)) {
            $('#capError').text('Il CAP deve contenere 5 cifre numeriche.');
            return;
        }
        if (!viaRegex.test(via)) {
            $('#viaError').text('La via deve contenere tra 2 e 45 caratteri.');
            return;
        }
        if (!civicoRegex.test(civico)) {
            $('#civicoError').text('Il civico deve contenere tra 1 e 10 caratteri.');
            return;
        }
        if (!cittaRegex.test(citta)) {
            $('#cittaError').text('La città deve contenere tra 2 e 45 caratteri.');
            return;
        }

        // AJAX per aggiungere l'indirizzo
        $.ajax({
            url: "/aggiungiIndirizzoSpedizione",
            type: "POST",
            data: { provincia: provincia, cap: cap, via: via, civico: civico, citta: citta },
            success: function (response) {
                // Aggiungi il nuovo indirizzo al campo di selezione
                const nuovoIndirizzo = `<option value="${provincia}-${cap}-${via}-${civico}-${citta}">
                    ${provincia} ${cap} ${via} ${civico} ${citta}
                </option>`;
                $('#indirizzo').append(nuovoIndirizzo);

                // Mostra un messaggio di successo
                Swal.fire('Successo', 'Indirizzo aggiunto correttamente!', 'success');

                // Svuota i campi del form
                $('#myFormIndirizzo')[0].reset(); // Usa .reset() per svuotare il form

                // Chiudi il form
                closeForm('myFormIndirizzo');
            },
            error: function (xhr) {
                Swal.fire('Errore', xhr.responseJSON?.message || 'Errore durante l\'aggiunta dell\'indirizzo.', 'error');
            }
        });
    });
});
    // Funzione per aggiornare la lista degli indirizzi dal backend
    function aggiornaListaIndirizzi() {
        $.ajax({
            url: "/aggiornaIndirizzi",
            type: "GET",
            success: function(response) {
                $('#indirizzo').empty();
                $('#indirizzo').append('<option value="">Seleziona indirizzo</option>');

                if (response && response.indirizzi) {
                    $.each(response.indirizzi, function(index, is) {
                        let option = `<option value="${is.id.provincia}-${indirizzo.id.cap}-${is.id.via}-${is.id.civico}-${is.id.citta}">
                            ${is.id.provincia} ${is.id.cap} ${is.id.via} ${is.id.civico} ${is.id.citta}
                        </option>`;
                        $('#indirizzo').append(option);
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error("Errore nel recupero degli indirizzi:", error);
            }
        });
    }



function openForm(formId) {
    const formElement = document.getElementById(formId);
    if (formElement) {
        formElement.style.display = "block";
    } else {
        console.error("Form element with ID", formId, "not found");
    }
}

function closeForm(formId) {
    const formElement = document.getElementById(formId);
    if (formElement) {
        formElement.style.display = "none";
    } else {
        console.error("Form element with ID", formId, "not found");
    }
}

$(document).ready(function() {
    $('#indirizzo, #cartacredito').change(function() {
        const indirizzoValue = $('#indirizzo').val();
        const cartaCreditoValue = $('#cartacredito').val();
        $('.processa-ordine').prop('disabled', indirizzoValue === '' || cartaCreditoValue === '');
    });
});


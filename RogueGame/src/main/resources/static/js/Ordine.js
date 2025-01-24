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
            error: function (jqXHR, textStatus, errorThrown) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Si è verificato un errore durante l\'elaborazione dell\'ordine.'
                });
            }
        })
    })
})

    $(document).ready(function () {
    // Cerca prodotti lato client
    $('#search-bar').on('input', function () {
        const searchTerm = $(this).val().toLowerCase();

        $('.prodotto').each(function () {
            const productName = $(this).find('h3').text().toLowerCase();

            if (productName.includes(searchTerm)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});

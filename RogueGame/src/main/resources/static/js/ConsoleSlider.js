document.addEventListener('DOMContentLoaded', function () {
    const productContainerConsole = document.getElementById('productContainerConsole');
    const productsConsole = productContainerConsole.getElementsByClassName('product-card-console');
    const prevBtnConsole = document.getElementById('prev-slide-Console');
    const nextBtnConsole = document.getElementById('next-slide-Console');
    const itemsPerPageConsole = 3;
    let currentIndexConsole = 0;

    function showSlidesConsole(index) {
        for (let i = 0; i < productsConsole.length; i++) {
            productsConsole[i].style.display = 'none';
        }
        for (let i = 0; i < itemsPerPageConsole; i++) {
            const productIndex = (index + i) % productsConsole.length;
            productsConsole[productIndex].style.display = 'block';
        }
    }

    function nextSlideConsole() {
        currentIndexConsole = (currentIndexConsole + 1) % productsConsole.length;
        showSlidesConsole(currentIndexConsole);
    }

    function prevSlideConsole() {
        currentIndexConsole = (currentIndexConsole - 1 + productsConsole.length) % productsConsole.length;
        showSlidesConsole(currentIndexConsole);
    }

    nextBtnConsole.addEventListener('click', nextSlideConsole);
    prevBtnConsole.addEventListener('click', prevSlideConsole);

    showSlidesConsole(currentIndexConsole); // Mostra i primi prodotti inizialmente
});

document.addEventListener('DOMContentLoaded', function () {
    const productContainer = document.getElementById('productContainerFantasy');
    const products = productContainer.getElementsByClassName('product-card');
    const prevBtn = document.getElementById('prev-slide-fantasy');
    const nextBtn = document.getElementById('next-slide-fantasy');
    const itemsPerPage = 3;
    let currentIndex = 0;

    function showSlides(index) {
        for (let i = 0; i < products.length; i++) {
            products[i].style.display = 'none';
        }
        for (let i = 0; i < itemsPerPage; i++) {
            const productIndex = (index + i) % products.length;
            products[productIndex].style.display = 'block';
        }
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % products.length;
        showSlides(currentIndex);
    }

    function prevSlide() {
        currentIndex = (currentIndex - 1 + products.length) % products.length;
        showSlides(currentIndex);
    }

    nextBtn.addEventListener('click', nextSlide);
    prevBtn.addEventListener('click', prevSlide);

    showSlides(currentIndex); // Mostra i primi prodotti inizialmente
});

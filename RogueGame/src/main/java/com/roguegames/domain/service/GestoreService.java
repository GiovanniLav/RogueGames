package com.roguegames.domain.service;

import com.roguegames.domain.entity.Prodotto;

import java.util.List;

public interface GestoreService {
    // Ottieni tutti i prodotti
    List<Prodotto> getAllProducts();

    // Salva un prodotto
    void saveProduct(Prodotto prodotto);

    // Modifica un prodotto
    void updateProduct(Prodotto prodotto);

    // Elimina un prodotto
    void deleteProduct(String nome);

    // Ottieni un prodotto per nome
    Prodotto getProductByName(String nome);
}

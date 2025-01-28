package com.roguegames.domain.service;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.GestoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestoreService {

    @Autowired
    private GestoreRepository prodottoRepository; // Iniezione del repository

    // Ottieni tutti i prodotti
    public List<Prodotto> getAllProducts() {
        return prodottoRepository.findAll();
    }

    // Salva un prodotto
    public void saveProduct(Prodotto prodotto) {
        prodottoRepository.save(prodotto);
    }

    // Modifica un prodotto
    public void updateProduct(Prodotto prodotto) {
        prodottoRepository.save(prodotto);
    }

    // Elimina un prodotto
    public void deleteProduct(String nome) {
        prodottoRepository.deleteById(nome);
    }

    // Ottieni un prodotto per nome
    public Prodotto getProductByName(String nome) {
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(nome); // Chiamata non statica tramite l'istanza
        return prodottoOpt.orElse(null); // Se non trovato, restituisce null
    }
}


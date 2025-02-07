package com.roguegames.domain.service;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.GestoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestoreServiceImp implements GestoreService {

    @Autowired
    private GestoreRepository prodottoRepository; // Iniezione del repository

    // Ottieni tutti i prodotti
    @Override
    public List<Prodotto> getAllProducts() {
        return prodottoRepository.findAll();
    }

    // Salva un prodotto
    @Override
    public void saveProduct(Prodotto prodotto) {
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(prodotto.getNome());
        if(prodottoOpt.isPresent())
            throw new IllegalArgumentException("Esiste già un prodotto con questo nome.");
        else
            prodottoRepository.save(prodotto);

    }

    // Modifica un prodotto
    @Override
    public void updateProduct(Prodotto prodotto) {
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(prodotto.getNome());
        if(prodottoOpt.isPresent())
            prodottoRepository.save(prodotto);
        else
            throw new IllegalArgumentException("Non esiste un prodotto con questo nome.");
    }

    // Elimina un prodotto
    @Override
    public void deleteProduct(String nome) {
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(nome);
        if(prodottoOpt.isPresent())
            prodottoRepository.deleteById(nome);
        else
            throw new IllegalArgumentException("Non esiste un prodotto con questo nome.");

    }

    // Ottieni un prodotto per nome
    @Override
    public Prodotto getProductByName(String nome) {
        Optional<Prodotto> prodottoOpt = prodottoRepository.findById(nome); // Chiamata non statica tramite l'istanza
        return prodottoOpt.orElse(null); // Se non trovato, restituisce null
    }
}


package com.roguegames.domain.service;

import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CarrelloRepository;

import com.roguegames.domain.repository.PCarrelloRepository;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Validated
public class CarrelloServiceImp implements CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private PCarrelloRepository pCarrelloRepository;
    @Autowired
    private ProdottoService prodottoService;

    public Optional<PCarrello> trovaElementoCarrello(String nomeProdotto, String emailCliente) {
        PCarrelloId id = new PCarrelloId(nomeProdotto, emailCliente);
        return pCarrelloRepository.findById(id);
    }

    public List<PCarrello> getPCarrello(List<CarrelloItem> carrello) {
        List <PCarrello> carrelloLista = new ArrayList<>();
        for (CarrelloItem carrelloItem : carrello) {
            carrelloLista.add(carrelloItem.getCarrello());
        }
        return carrelloLista;
    }

    public static class QuantitaNonDisponibileException extends RuntimeException {
        public QuantitaNonDisponibileException(String message) {
            super(message);
        }
    }

    public void aggiungiProdotto(Prodotto prodotto, Utente utente) {
        PCarrelloId id = new PCarrelloId(prodotto.getNome(), utente.getEmail());
        Optional<PCarrello> prodottoCarrello = trovaElementoCarrello(prodotto.getNome(), utente.getEmail());
        if (prodottoCarrello.isPresent()) {
                PCarrello carrello = prodottoCarrello.get();
                int newQnt= carrello.getQuantita()+1;
                if(newQnt <= prodotto.getQuantita()) {
                    carrello.setQuantita(newQnt);
                    carrelloRepository.save(carrello);
                }else{throw new QuantitaNonDisponibileException("La quantità non è disponibile. La quantità massima di " + prodotto.getNome() + " è di " + prodotto.getQuantita());}
        }else{PCarrello carrello  = new PCarrello(id, 1, prodotto, utente);
            carrelloRepository.save(carrello);

        }
    }

    public void rimuoviProdotto(Prodotto prodotto, Utente utente) {
        Optional<PCarrello> prodottoCarrello = trovaElementoCarrello(prodotto.getNome(), utente.getEmail());
        if (prodottoCarrello.isPresent()) {
            carrelloRepository.delete(prodottoCarrello.get());
        }else{throw new QuantitaNonDisponibileException("Prodotto non presente");}
    }

    public void rimuoviInteroCarrello (List <PCarrello> carrello, Utente utente){
        if (!carrello.isEmpty() && carrello != null) {
            for (PCarrello cart : carrello) {
                carrelloRepository.delete(cart);
            }
        }
    }

    private static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false; // Stringa vuota o nulla non è un intero
        }
        try {
            Integer.parseInt(str);
            return true; // La conversione è andata a buon fine
        } catch (NumberFormatException e) {
            return false; // La stringa non rappresenta un intero
        }
    }

    public void modificaQnt(Prodotto prodotto, Utente utente, String quantitaStr) {
        PCarrelloId id = new PCarrelloId(utente.getEmail(), prodotto.getNome());
        Optional<PCarrello> prodottoCarrello = trovaElementoCarrello(prodotto.getNome(), utente.getEmail());
        prodotto = prodottoService.findProdotto(prodotto.getNome());
        if (prodottoCarrello.isPresent()) {

            PCarrello carrello = prodottoCarrello.get();

            if (!isInteger(quantitaStr)) {
                throw new QuantitaNonDisponibileException("La quantità deve essere un numero intero valido.");
            }
            System.out.println(prodottoCarrello.isPresent());
            int quantita = Integer.parseInt(quantitaStr);
            System.out.println(prodottoCarrello.isPresent());
            if(quantita <=0) {
                throw new QuantitaNonDisponibileException("La quantità non può essere negativa");
            }

            if(quantita > prodotto.getQuantita()) {
                throw new QuantitaNonDisponibileException("La quantità non è disponibile. La quantità massima di " + prodotto.getNome() + " è di " + prodotto.getQuantita());
            }

            carrello.setQuantita(quantita);
            carrelloRepository.save(carrello);

        }else {
            throw new QuantitaNonDisponibileException("Prodotto non presente");
        }
    }

    public List<PCarrello> getCarrello(Utente utente) {
        return (List<PCarrello>) carrelloRepository.findAllByUtente(utente);
    }

    public List<PCarrello> getCarrelloNome(String nome) {
        return (List<PCarrello>) carrelloRepository.findAllByNome(nome);
    }
}


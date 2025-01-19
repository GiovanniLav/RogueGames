package com.roguegames.domain.service;

import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.CarrelloRepository;

import com.roguegames.domain.repository.PCarrelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static com.mysql.cj.conf.PropertyKey.logger;


@Service
@Validated
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private PCarrelloRepository pCarrelloRepository;

    public Optional<PCarrello> trovaElementoCarrello(String emailCliente, String nomeProdotto) {
        PCarrelloId id = new PCarrelloId(nomeProdotto, emailCliente);
        return pCarrelloRepository.findById(id);
    }

    public void aggiungiProdotto(Prodotto prodotto, Utente utente) {
        PCarrelloId id = new PCarrelloId(utente.getEmail(), prodotto.getNome());
        Optional<PCarrello> prodottoCarrello = trovaElementoCarrello(utente.getEmail(), prodotto.getNome());
        if (prodottoCarrello.isPresent()) {
                PCarrello carrello = prodottoCarrello.get();
                carrello.setQuantita(carrello.getQuantita()+1);
                carrelloRepository.save(carrello);
        }else{PCarrello carrello  = new PCarrello(id, 1, prodotto, utente);
            carrelloRepository.save(carrello);
        }
    }

    public void rimuoviProdotto(Prodotto prodotto, Utente utente) {
        PCarrelloId id = new PCarrelloId(utente.getEmail(), prodotto.getNome());
        Optional<PCarrello> prodottoCarrello = trovaElementoCarrello(utente.getEmail(), prodotto.getNome());
        if (prodottoCarrello.isPresent()) {
            carrelloRepository.delete(prodottoCarrello.get());
        }
    }

    public List<PCarrello> getCarrello(Utente utente) {
        return (List<PCarrello>) carrelloRepository.findAllByUtente(utente);
    }

}


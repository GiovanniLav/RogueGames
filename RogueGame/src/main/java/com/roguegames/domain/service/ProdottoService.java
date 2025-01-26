package com.roguegames.domain.service;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import com.roguegames.web.controller.Item.CarrelloItem;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    public List<Prodotto> findProdotto(List <CarrelloItem> carrello) {
        List <Prodotto> prodotto = new ArrayList<Prodotto>();
        for (CarrelloItem carrelloItem : carrello) {
            prodotto.add(carrelloItem.getCarrello().getProdotto());
        }
        return prodotto;
    }

    public List<Prodotto> getAllProdotti() {return prodottoRepository.findAll();}

    public Prodotto saveProdotto(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
        }

    public Prodotto findProdotto(String nome){return prodottoRepository.findByNome(nome);}

    public void updateProdotto(List<Prodotto> prodotto) {
        for (Prodotto p : prodotto) {
            Prodotto prodotto1 = prodottoRepository.findByNome(p.getNome());
            if (prodotto1 != null) {
                int qnt=prodotto1.getQuantita();
                int qnr2= p.getQuantita();
                prodotto1.setQuantita(qnt-qnr2);
                prodottoRepository.save(prodotto1);
            }
        }
    }

    public List<Prodotto> get6RandomProdotto() {return prodottoRepository.findRandom();}
    public List<Prodotto> filteredCatalogo(Prodotto.Piattaforma piattaforma) {return prodottoRepository.findByPiattaforma(piattaforma);};
}

package com.roguegames.domain.service;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.ProdottoRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.roguegames.web.controller.Item.CarrelloItem;

@Service
public class ProdottoServiceImp implements ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Override
    public List<Prodotto> findProdotto(List<CarrelloItem> carrello) {
        List <Prodotto> prodotto = new ArrayList<Prodotto>();
        for (CarrelloItem carrelloItem : carrello) {
            prodotto.add(carrelloItem.getCarrello().getProdotto());
        }
        return prodotto;
    }

    @Override
    public List<Prodotto> getAllProdotti() {return prodottoRepository.findAll();}

    @Override
    public Prodotto saveProdotto(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
        }

    @Override
    public Prodotto findProdotto(String nome){return prodottoRepository.findByNome(nome);}

    @Override
    public void updateProdottoQnt(@NotNull Prodotto prodotto, int qnt) {
                int qnt2= prodotto.getQuantita();
                prodotto.setQuantita(qnt2-qnt);
                prodottoRepository.save(prodotto);
            }

    @Override
    public List<Prodotto> get6RandomProdotto() {return prodottoRepository.findRandom();}

    @Override
    public List<Prodotto> filteredCatalogo(Prodotto.Piattaforma piattaforma) {return prodottoRepository.findByPiattaforma(piattaforma);};

    @Override
    public List<Prodotto> getFantasy() {return prodottoRepository.findFantasy();};

    @Override
    public List<Prodotto> getConsole() {return prodottoRepository.findConsole();};

    @Override
    public List<Prodotto> ordinaPerData(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return prodottoRepository.findAllByOrderByDataRilascioDesc();
        } else {
            return prodottoRepository.findAllByOrderByDataRilascioAsc();
        }
    }
}




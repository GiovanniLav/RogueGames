package com.roguegames.domain.service;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ProdottoService {
    List<Prodotto> findProdotto(List<CarrelloItem> carrello);

    List<Prodotto> getAllProdotti();

    Prodotto saveProdotto(Prodotto prodotto);

    Prodotto findProdotto(String nome);

    void updateProdottoQnt(@NotNull Prodotto prodotto, int qnt);

    List<Prodotto> get6RandomProdotto();

    List<Prodotto> filteredCatalogo(Prodotto.Piattaforma piattaforma);

    List<Prodotto> getFantasy();

    List<Prodotto> getConsole();

    List<Prodotto> ordinaPerData(String order);
}

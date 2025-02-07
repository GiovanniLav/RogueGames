package com.roguegames.domain.service;

import com.roguegames.domain.entity.*;
import com.roguegames.domain.repository.OrdineRepository;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrdineServiceImp implements OrdineService {


    @Autowired
    OrdineRepository ordineRepository;
    @Autowired
    private CarrelloService carrelloService;




    @Override
    public void processaOrdine(Utente utente, List<CarrelloItem> carrello1, double totale) {

        if (carrello1 == null || carrello1.isEmpty()) {
            throw new IllegalArgumentException("Il carrello non può essere vuoto.");
        }

        Date data = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = formatter.format(data);

        double prz;
        int qnt;
        double prezzoQnt;
        List <Ordine> ordine = new ArrayList<Ordine>();
        List <CarrelloItem> carrello = new ArrayList<>(carrello1);
        CarrelloItem c = carrello.remove(0);

        prz = c.getPrezzo();
        qnt = c.getCarrello().getQuantita();
        prezzoQnt = prz * qnt;
        String nome=c.getCarrello().getId().getNome();
        String email=utente.getEmail();
        OrdineId id =new OrdineId(nome, email, date);
        Ordine order = new Ordine(id, prezzoQnt, totale, qnt, false, utente);

            ordineRepository.save(order);
            ordineRepository.flush();

        Ordine p = ordineRepository.findByNomeAndEmailAndData(nome, email, date);

        int i=p.getId().getIdOrdine();

        if (carrello != null || !carrello.isEmpty()) {
            for (CarrelloItem item : carrello) {
                prz = item.getPrezzo();
                qnt = item.getCarrello().getQuantita();
                prezzoQnt = prz * qnt;
                id = new OrdineId(item.getCarrello().getId().getNome(), utente.getEmail(), date);
                id.setIdOrdine(i);
                order = new Ordine(id, prezzoQnt, totale, qnt, false, utente);


                order = ordineRepository.saveAndFlush(order);
                ordine.add(order);
            }
        }
    }

    @Override
    public List<Ordine> getOrdine() {
        return ordineRepository.findAll();
    }

    @Override
    public List<Ordine> getOrdineUtente(String email) {
        return ordineRepository.findByEmail(email);
    }

}

package com.roguegames.domain.entity;

import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Preferiti {

    @EmbeddedId
    private PreferitiId id;  // Chiave primaria composta

    private String immagine;

    @ManyToOne
    @JoinColumn(name = "Nome", referencedColumnName = "Nome", insertable = false, updatable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "Email", referencedColumnName = "Email", insertable = false, updatable = false)
    private Utente utente;

    // Costruttori
    public Preferiti() {}

    public Preferiti(PreferitiId id, String immagine) {
        this.id = id;
        this.immagine = immagine;
    }

    // Getters e Setters
    public PreferitiId getId() {
        return id;
    }

    public void setId(PreferitiId id) {
        this.id = id;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}


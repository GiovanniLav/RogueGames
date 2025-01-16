package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "pcarrello")
public class PCarrello {

    @EmbeddedId
    private PCarrelloId id;

    @Column(name = "Quantita", nullable = false)
    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 1, message = "La quantità deve essere almeno 1")
    private Integer quantita;

    @ManyToOne
    @MapsId("nome")
    @JoinColumn(name = "Nome", referencedColumnName = "Nome", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @MapsId("email")
    @JoinColumn(name = "Email", referencedColumnName = "Email", nullable = false)
    private Utente utente;

    // Costruttori
    public PCarrello() {}

    public PCarrello(PCarrelloId id, Integer quantita, Prodotto prodotto, Utente utente) {
        this.id = id;
        this.quantita = quantita;
        this.prodotto = prodotto;
        this.utente = utente;
    }

    // Getters e Setters
    public PCarrelloId getId() {
        return id;
    }

    public void setId(PCarrelloId id) {
        this.id = id;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
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

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PCarrello that = (PCarrello) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

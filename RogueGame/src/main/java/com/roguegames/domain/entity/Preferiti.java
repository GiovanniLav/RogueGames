package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "preferiti")
public class Preferiti {

    @EmbeddedId
    private PreferitiId id;

    @Column(name = "Immagine", length = 45, nullable = false)
    @NotBlank(message = "Il campo immagine è obbligatorio")
    private String immagine;

    @ManyToOne
    @MapsId("nome")
    @JoinColumn(name = "Nome", referencedColumnName = "Nome", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @MapsId("email")
    @JoinColumn(name = "Email", referencedColumnName = "Email", nullable = false)
    private Utente utente;

    // Costruttori
    public Preferiti() {}

    public Preferiti(PreferitiId id, String immagine, Prodotto prodotto, Utente utente) {
        this.id = id;
        this.immagine = immagine;
        this.prodotto = prodotto;
        this.utente = utente;
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

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preferiti preferiti = (Preferiti) o;

        return id.equals(preferiti.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

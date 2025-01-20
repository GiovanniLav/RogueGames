package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "ordine")
public class Ordine {

    @EmbeddedId
    private OrdineId id;

    @Column(name = "Prezzo", nullable = false)
    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere un valore positivo")
    private Double prezzo;

    @Column(name = "PrezzoTot", nullable = false)
    @NotNull(message = "Il prezzo totale è obbligatorio")
    @Positive(message = "Il prezzo totale deve essere un valore positivo")
    private Double prezzoTot;

    @Column(name = "Quantita", nullable = false)
    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 1, message = "La quantità deve essere almeno 1")
    private Integer quantita;

    @Column(name = "Stato", nullable = false)
    private Boolean stato;

    @Column(name = "Data", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "La data è obbligatoria")
    private LocalDate data;

    @ManyToOne
    @MapsId("email")
    @JoinColumn(name = "Email", referencedColumnName = "Email", nullable = false)
    private Utente utente;

    // Costruttori
    public Ordine() {}

    public Ordine(OrdineId id, Double prezzo, Double prezzoTot, Integer quantita, Boolean stato, LocalDate data, Utente utente) {
        this.id = id;
        this.prezzo = prezzo;
        this.prezzoTot = prezzoTot;
        this.quantita = quantita;
        this.stato = stato;
        this.data = data;
        this.utente = utente;
    }

    // Getters e Setters
    public OrdineId getId() {
        return id;
    }

    public void setId(OrdineId id) {
        this.id = id;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Double getPrezzoTot() {
        return prezzoTot;
    }

    public void setPrezzoTot(Double prezzoTot) {
        this.prezzoTot = prezzoTot;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Boolean getStato() {
        return stato;
    }

    public void setStato(Boolean stato) {
        this.stato = stato;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

        Ordine ordine = (Ordine) o;

        return id.equals(ordine.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}


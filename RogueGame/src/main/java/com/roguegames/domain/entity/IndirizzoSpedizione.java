package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "indirizzospedizione")
public class IndirizzoSpedizione {

    @EmbeddedId
    private IndirizzoSpedizioneId id;

    @ManyToOne
    @MapsId("email")
    @JoinColumn(name = "Email", referencedColumnName = "Email", nullable = false)
    private Utente utente;

    // Costruttori
    public IndirizzoSpedizione() {}

    public IndirizzoSpedizione(IndirizzoSpedizioneId id, Utente utente) {
        this.id = id;
        this.utente = utente;
    }

    public IndirizzoSpedizioneId getId() { return id; }
    public void setId(IndirizzoSpedizioneId id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }


    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndirizzoSpedizione that = (IndirizzoSpedizione) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
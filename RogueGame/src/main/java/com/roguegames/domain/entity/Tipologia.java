package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tipologia")
public class Tipologia {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "Tipologia", nullable = false, unique = true)
    @NotNull(message = "La tipologia è obbligatoria")
    private Tipo tipologia;

    public enum Tipo {
        Videogiochi,
        Console,
        AF,
        Accessori
    }

    // Costruttori
    public Tipologia() {}

    public Tipologia(Tipo tipologia) {
        this.tipologia = tipologia;
    }

    // Getter e Setter
    public Tipo getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipo tipologia) {
        this.tipologia = tipologia;
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tipologia tipologia1 = (Tipologia) o;

        return tipologia == tipologia1.tipologia;
    }

    @Override
    public int hashCode() {
        return tipologia != null ? tipologia.hashCode() : 0;
    }
}

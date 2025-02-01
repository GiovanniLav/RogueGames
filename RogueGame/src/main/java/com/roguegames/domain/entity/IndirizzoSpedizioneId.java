package com.roguegames.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class IndirizzoSpedizioneId implements Serializable {

    @Column(name = "Provincia", length = 3, nullable = false)
    @NotBlank(message = "La provincia è obbligatoria")
    private String provincia;

    @Column(name = "CAP", nullable = false)
    @NotNull(message = "Il CAP è obbligatorio")
    @Digits(integer = 5, fraction = 0, message = "Il CAP deve contenere 5 cifre")
    private Integer cap;

    @Column(name = "Via", length = 45, nullable = false)
    @NotBlank(message = "La via è obbligatoria")
    private String via;

    @Column(name = "Civico", length = 10, nullable = false)
    @NotBlank(message = "Il numero civico è obbligatorio")
    private String civico;

    @Column(name = "Citta", length = 45, nullable = false)
    @NotBlank(message = "La città è obbligatoria")
    private String citta;

    @NotNull(message = "L'email è obbligatoria")
    private String email;

    public IndirizzoSpedizioneId() {}

    public IndirizzoSpedizioneId(String provincia, Integer cap, String via, String civico, String citta, String email) {
        this.provincia = provincia;
        this.cap = cap;
        this.via = via;
        this.civico = civico;
        this.citta = citta;
        this.email = email;
    }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public Integer getCap() { return cap; }
    public void setCap(Integer cap) { this.cap = cap; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getCivico() { return civico; }
    public void setCivico(String civico) { this.civico = civico; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public String getEmail() { return email; }
    public void setEmai(String email) { this.email = email; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndirizzoSpedizioneId indirizzoSpedizioneId = (IndirizzoSpedizioneId) o;

        return  Objects.equals(provincia, indirizzoSpedizioneId.provincia) &&
                Objects.equals(cap, indirizzoSpedizioneId.cap) &&
                Objects.equals(via, indirizzoSpedizioneId.via) &&
                Objects.equals(civico, indirizzoSpedizioneId.civico)&&
                Objects.equals(citta, indirizzoSpedizioneId.citta) &&
                Objects.equals(email, indirizzoSpedizioneId.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provincia, cap, via, civico, citta, email);
    }

}

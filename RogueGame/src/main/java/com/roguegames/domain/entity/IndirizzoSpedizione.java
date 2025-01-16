package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "indirizzospedizione")
public class IndirizzoSpedizione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "Email", nullable = false)
    @NotNull(message = "L'email è obbligatoria")
    private Utente utente;

    // Costruttori
    public IndirizzoSpedizione() {}

    public IndirizzoSpedizione(String provincia, Integer cap, String via, String civico, String citta, Utente utente) {
        this.provincia = provincia;
        this.cap = cap;
        this.via = via;
        this.civico = civico;
        this.citta = citta;
        this.utente = utente;
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "cartadicredito")
public class CartaDiCredito {

    @Id
    @Column(name = "CIF", length = 18, nullable = false, unique = true)
    @NotBlank(message = "Il CIF è obbligatorio")
    @Size(min = 16, max = 16, message = "Il CIF deve contenere 16 cifre")
    private String cif;

    @Column(name = "Scadenza", length = 10, nullable = false)
    @NotBlank(message = "La scadenza è obbligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "La scadenza deve essere nel formato MM/YY")
    private String scadenza;


    @Column(name = "CVV", length = 4, nullable = false)
    @NotBlank(message = "Il CVV è obbligatorio")
    @Pattern(regexp = "\\d{3}", message = "Il CVV deve contenere 3 cifre")
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "EmailUT", nullable = false)
    @NotNull(message = "L'email associata è obbligatoria")
    private Utente utente;

    // Costruttori
    public CartaDiCredito() {}

    public CartaDiCredito(String cif, String scadenza, String cvv, Utente utente) {
        this.cif = cif;
        this.scadenza = scadenza;
        this.cvv = cvv;
        this.utente = utente;
    }

    // Getters e setters
    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }

    public String getScadenza() { return scadenza; }
    public void setScadenza(String scadenza) { this.scadenza = scadenza; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartaDiCredito that = (CartaDiCredito) o;

        return cif.equals(that.cif);
    }

    @Override
    public int hashCode() {
        return cif.hashCode();
    }

}
package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;

@Entity
@Table(name = "clientereg")
public class Utente {

    @Id
    @Column(name = "Email", length = 45, unique = true)
    @NotBlank(message = "L'email è obbligatoria")
    @Size(min = 6, max = 45, message = "L'email deve essere tra 6 e 45 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Formato email non valido. Deve avere alemno 2 lettere dopo il punto")
    private String email;

    @Column(name = "Password", length = 100)
    @NotBlank(message = "La password è obbligatoria")
    private String password;

    @Column(name = "Nome", length = 45)
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 45, message = "Il nome deve essere tra 2 e 45 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9]{2,45}", message = "Il nome può contenere solo lettere e numeri, tra 2 e 45 caratteri")
    private String nome;

    @Column(name = "Cognome", length = 45)
    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 45, message = "Il cognome deve essere tra 2 e 45 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9]{2,45}", message = "Il cognome può contenere solo lettere e numeri, tra 2 e 45 caratteri")
    private String cognome;

    @Column(name = "Eta")
    @NotNull(message = "L'età è obbligatoria")
    @Min(value = 10, message = "L'età deve essere almeno 10 anni")
    @Max(value = 120, message = "L'età non può superare i 120 anni")
    private int eta;

    @Column(name = "Residenza", length = 45)
    @NotBlank(message = "La residenza è obbligatoria")
    @Size(min = 7, max = 45, message = "L'indirizzo di residenza deve essere tra 7 e 45 caratteri")
    @Pattern(regexp = "^[a-zA-Z0-9 ]{7,45}", message = "L'indirizzo può contenere solo lettere, numeri e spazi, tra 7 e 45 caratteri")
    private String residenza;

    @Column(name = "Tel", length = 10)
    @NotBlank(message = "Il numero di telefono è obbligatorio")
    @Pattern(regexp = "^[0-9]{10}", message = "Il numero di telefono deve essere esattamente di 10 cifre numeriche")
    private String tel;

    @Column(name = "Ruolo", length = 45)
    private String ruolo = "utente"; // Valore di default

    @Column(name = "Punti")
    @PositiveOrZero(message = "I punti devono essere zero o positivi")
    private int punti = 0; // Valore di default

    // Costruttori (necessario il costruttore vuoto per JPA)
    public Utente() {}

    public Utente(String email, String password, String nome, String cognome, int eta, String residenza, String tel) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.residenza = residenza;
        this.tel = tel;
    }


    // Getters e setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public int getEta() { return eta; }
    public void setEta(int eta) { this.eta = eta; }

    public String getResidenza() { return residenza; }
    public void setResidenza(String residenza) { this.residenza = residenza; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public int getPunti() { return punti; }
    public void setPunti(int punti) { this.punti = punti; }

    // Equals e HashCode (importanti per le Entity)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utente utente = (Utente) o;

        return email.equals(utente.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}

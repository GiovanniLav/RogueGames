package com.roguegames.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @Column(name = "Nome", length = 45, nullable = false)
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 45, message = "Il nome non può superare i 45 caratteri")
    private String nome;

    @Column(name = "Immagine", length = 255, nullable = false)
    @NotBlank(message = "L'immagine è obbligatoria")
    private String immagine;

    @Column(name = "Video", length = 255)
    private String video;

    @Column(name = "Descrizione", columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    @Column(name = "CoV", nullable = false)
    private boolean cov = false;

    @Column(name = "Prezzo", nullable = false)
    @NotNull(message = "Il prezzo è obbligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Il prezzo deve essere maggiore di zero")
    private Double prezzo;

    @Column(name = "CasaProd", length = 45, nullable = false)
    @NotBlank(message = "La casa produttrice è obbligatoria")
    @Size(max = 45, message = "Il nome della casa produttrice non può superare i 45 caratteri")
    private String casaProd;

    @Column(name = "Piattaforma", nullable = true)
    @Enumerated(EnumType.STRING)
    private Piattaforma piattaforma;

    @Column(name = "Genere", length = 45, nullable = false)
    @NotBlank(message = "Il genere è obbligatorio")
    @Size(max = 45, message = "Il genere non può superare i 45 caratteri")
    private String genere;

    @Column(name = "Tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Il tipo è obbligatorio")
    private Tipo tipo;

    @Column(name = "DataRilascio", nullable = false)
    @NotNull(message = "La data di rilascio è obbligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataRilascio;

    @Column(name = "Quantita", nullable = false)
    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 0, message = "La quantità deve essere almeno 0")
    private Integer quantita;

    @ManyToOne
    @JoinColumn(name = "Tipo", referencedColumnName = "Tipologia", nullable = false, insertable = false, updatable = false)
    private Tipologia tipologia;

    // Nuovo campo preferito
    @Transient  // Non salvato nel database, solo in memoria
    private boolean preferito;

    // Enumerazioni per Piattaforma e Tipo
    public enum Piattaforma {
        PlayStation, Xbox, Pc, NintendoSwitch, Nessuna
    }

    public enum Tipo {
        Videogiochi, Console, AF, Accessori
    }

    // Costruttori
    public Prodotto() {
    }

    public Prodotto(String nome, String immagine, String video, String descrizione, boolean cov, Double prezzo,
                    String casaProd, Piattaforma piattaforma, String genere, Tipo tipo, Date dataRilascio, Integer quantita) {
        this.nome = nome;
        this.immagine = immagine;
        this.video = video;
        this.descrizione = descrizione;
        this.cov = cov;
        this.prezzo = prezzo;
        this.casaProd = casaProd;
        this.piattaforma = piattaforma;
        this.genere = genere;
        this.tipo = tipo;
        this.dataRilascio = dataRilascio;
        this.quantita = quantita;
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isCov() {
        return cov;
    }

    public void setCov(boolean cov) {
        this.cov = cov;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public String getCasaProd() {
        return casaProd;
    }

    public void setCasaProd(String casaProd) {
        this.casaProd = casaProd;
    }

    public Piattaforma getPiattaforma() {
        return piattaforma;
    }

    public void setPiattaforma(Piattaforma piattaforma) {
        this.piattaforma = piattaforma;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Date getDataRilascio() {
        return dataRilascio;
    }

    public void setDataRilascio(Date dataRilascio) {
        this.dataRilascio = dataRilascio;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

    // Getter e Setter per il campo preferito
    public boolean isPreferito() {
        return preferito;
    }

    public void setPreferito(boolean preferito) {
        this.preferito = preferito;
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prodotto prodotto = (Prodotto) o;

        return nome.equals(prodotto.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}



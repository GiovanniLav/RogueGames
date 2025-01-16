package com.roguegames.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrdineId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdine", nullable = false)
    private Integer idOrdine;

    @Column(name = "Nome", length = 45, nullable = false)
    private String nome;

    @Column(name = "Email", length = 45, nullable = false)
    private String email;

    // Costruttori
    public OrdineId() {}

    public OrdineId(Integer idOrdine, String nome, String email) {
        this.idOrdine = idOrdine;
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
    public Integer getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdineId ordineId = (OrdineId) o;

        return Objects.equals(idOrdine, ordineId.idOrdine) &&
                Objects.equals(nome, ordineId.nome) &&
                Objects.equals(email, ordineId.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdine, nome, email);
    }
}

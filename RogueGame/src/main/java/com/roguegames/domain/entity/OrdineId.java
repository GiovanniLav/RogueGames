package com.roguegames.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class OrdineId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdine", nullable = false )
    private int idOrdine;

    @Column(name = "Nome", length = 45, nullable = false)
    private String nome;

    @Column(name = "Email", length = 45, nullable = false)
    private String email;

    @Column(name = "Data", nullable = false)
    @NotNull(message = "La data è obbligatoria")
    private String data;

    // Costruttori
    public OrdineId() {}

    public OrdineId(String nome, String email, String data) {
        this.nome = nome;
        this.email = email;
        this.data = data;
    }

    public OrdineId(int idOrdine, String nome, String email, String data) {
        this.idOrdine = idOrdine;
        this.nome = nome;
        this.email = email;
        this.data = data;
    }

    // Getters e Setters

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdineId ordineId = (OrdineId) o;

        return  Objects.equals(idOrdine, ordineId.idOrdine) &&
                Objects.equals(nome, ordineId.nome) &&
                Objects.equals(email, ordineId.email) &&
                Objects.equals(data, ordineId.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, data);
    }
}

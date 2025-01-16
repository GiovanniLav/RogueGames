package com.roguegames.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PreferitiId implements Serializable {

    @Column(name = "Nome", length = 45, nullable = false)
    private String nome;

    @Column(name = "Email", length = 45, nullable = false)
    private String email;

    // Costruttori
    public PreferitiId() {}

    public PreferitiId(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
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

        PreferitiId that = (PreferitiId) o;

        return Objects.equals(nome, that.nome) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email);
    }
}

package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Usuario {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id private long id;
    private String nome;
    private String username;
    private String email;
    private String senha;

    public Usuario(){}

    public Usuario(String nome, String username, String email, String senha) {
        this.nome = nome;
        this.username = username;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

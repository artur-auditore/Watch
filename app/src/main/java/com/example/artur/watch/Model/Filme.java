package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;

@Entity
public class Filme extends Item {

    public Filme(String titulo, String genero, int ano, String estrelando, String sinopse){
        this.setTitulo(titulo);
        this.setGenero(genero);
        this.setAno(ano);
        this.setEstrelando(estrelando);
        this.setSinopse(sinopse);
    }
}

package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;

@Entity
public class Filme extends Item {

    private ToOne<Usuario> usuario;

    public Filme() { }

    public Filme(String titulo, String genero, int ano, String estrelando, String sinopse){
        this.setTitulo(titulo);
        this.setGenero(genero);
        this.setAno(ano);
        this.setEstrelando(estrelando);
        this.setSinopse(sinopse);
    }

    public ToOne<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(ToOne<Usuario> usuario) {
        this.usuario = usuario;
    }
}

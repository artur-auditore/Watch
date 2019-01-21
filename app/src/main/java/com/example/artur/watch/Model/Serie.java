package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;

import java.util.List;

@Entity
public class Serie extends Item {

    private ToOne<Usuario> usuario;

    public Serie(String titulo, String genero, int ano, String estrelando, String sinopse){
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

    private List<Temporada> temporadas;

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
}

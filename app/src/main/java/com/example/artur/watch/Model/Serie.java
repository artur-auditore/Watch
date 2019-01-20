package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;

import java.util.List;

@Entity
public class Serie extends Item {

    public Serie(String titulo, String genero, int ano, String estrelando, String sinopse){
        this.setTitulo(titulo);
        this.setGenero(genero);
        this.setAno(ano);
        this.setEstrelando(estrelando);
        this.setSinopse(sinopse);
    }

    private List<Temporada> temporadas;

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
}

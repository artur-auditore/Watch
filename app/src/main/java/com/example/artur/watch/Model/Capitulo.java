package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Capitulo {

    @Id  private long id;
    private String titulo;
    private String descricao;
    private ToOne<Temporada> temporada;

    public Capitulo() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ToOne<Temporada> getTemporada() {
        return temporada;
    }

    public void setTemporada(ToOne<Temporada> temporada) {
        this.temporada = temporada;
    }
}

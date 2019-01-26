package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

import java.util.List;

@Entity
public class Serie {

    @Id private long id;
    private ToOne<Filme> filme;
    private int qtdTemporadas;
    private ToOne<Usuario> usuario;
    private List<Temporada> temporadas;


    public Serie() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ToOne<Filme> getFilme() {
        return filme;
    }

    public void setFilme(ToOne<Filme> filme) {
        this.filme = filme;
    }

    public int getQtdTemporadas() {
        return qtdTemporadas;
    }

    public void setQtdTemporadas(int qtdTemporadas) {
        this.qtdTemporadas = qtdTemporadas;
    }

    public ToOne<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(ToOne<Usuario> usuario) {
        this.usuario = usuario;
    }

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
}

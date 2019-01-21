package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

import java.util.List;

@Entity
public class Temporada {

    @Id private long id;
    private int numero;
    private String titulo;
    private String descricao;
    private ToOne<Serie> serie;
    private List<Capitulo> capitulos;

    public Temporada() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public ToOne<Serie> getSerie() {
        return serie;
    }

    public void setSerie(ToOne<Serie> serie) {
        this.serie = serie;
    }
}

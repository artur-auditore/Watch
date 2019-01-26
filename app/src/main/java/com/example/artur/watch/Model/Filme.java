package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Filme {

    @Id private long id;
    private String titulo;
    private String genero;
    private int ano;
    private String sinopse;
    private String estrelando;
    private String estudio;
    private ToOne<Usuario> usuario;

    public Filme() { }

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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getEstrelando() {
        return estrelando;
    }

    public void setEstrelando(String estrelando) {
        this.estrelando = estrelando;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public ToOne<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(ToOne<Usuario> usuario) {
        this.usuario = usuario;
    }
}

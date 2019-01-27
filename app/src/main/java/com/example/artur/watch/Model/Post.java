package com.example.artur.watch.Model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

import java.util.Date;
import java.util.List;

@Entity
public class Post {



    @Id private long id;
    private String descricao;
    private Date data;
    private boolean arquivado = false;
    private ToOne<Usuario> usuario;
    private ToOne<Filme> filme;
    private ToOne<Serie> serie;


    public Post() {}

    public Post(String descricao, Date data) {
        this.descricao = descricao;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public ToOne<Usuario> getUsuario() {
        return usuario;
    }

    public void setUsuario(ToOne<Usuario> usuario) {
        this.usuario = usuario;
    }

    public boolean isArquivado() {
        return arquivado;
    }

    public void setArquivado(boolean arquivado) {
        this.arquivado = arquivado;
    }

    public ToOne<Filme> getFilme() {
        return filme;
    }

    public void setFilme(ToOne<Filme> filme) {
        this.filme = filme;
    }

    public ToOne<Serie> getSerie() {
        return serie;
    }

    public void setSerie(ToOne<Serie> serie) {
        this.serie = serie;
    }
}

package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

import java.util.Date

@Entity
class Post {


    @Id
    var id: Long = 0
    lateinit var descricao: String
    lateinit var data: Date
    var isArquivado = false
    lateinit var usuario: ToOne<Usuario>
    lateinit var serie: ToOne<Serie>


    constructor() {}

    constructor(descricao: String, data: Date) {
        this.descricao = descricao
        this.data = data
    }

    override fun toString(): String {
        return "Título: " + serie.target.titulo + "\n" +
                "Ano: " + serie.target.ano + "\n" +
                "Gênero: " + serie.target.genero + "\n" +
                "Série Original " + serie.target.estudio + "\n"
    }

}

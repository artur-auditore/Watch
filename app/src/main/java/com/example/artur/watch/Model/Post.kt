package com.example.artur.watch.Model

import io.objectbox.annotation.*
import io.objectbox.relation.ToOne

import java.util.Date

@Entity
class Post{

    @Id
    var id: Long = 0
    lateinit var descricao: String
    lateinit var data: Date
    lateinit var estadoPost: String
    lateinit var usuario: ToOne<Usuario>
    lateinit var serie: ToOne<Serie>

    constructor()

    constructor(descricao: String, data: Date) {
        this.descricao = descricao
        this.data = data
    }

    override fun toString(): String {
        return if (serie.target.tipo == "Série")
            "Título: ${serie.target.titulo}\n" +
                    "Ano: ${serie.target.ano}\n" +
                    "Gênero: ${serie.target.genero}\n" +
                    "Série Original ${serie.target.estudio}\n" else
            "Título: ${serie.target.titulo}\n" +
                    "Ano: ${serie.target.ano}\n" +
                    "Gênero: ${serie.target.genero}\n" +
                    "Produzido por: ${serie.target.estudio}\n"
    }

}

package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class Capitulo {

    constructor()

    constructor(titulo: String, descricao: String){
        this.titulo = titulo
        this.descricao = descricao
    }

    @Id
    var id: Long = 0
    lateinit var titulo: String
    lateinit var descricao: String
    lateinit var temporada: ToOne<Temporada>
    lateinit var serie: ToOne<Serie>
}

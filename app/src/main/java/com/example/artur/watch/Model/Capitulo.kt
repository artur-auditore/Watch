package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class Capitulo {

    @Id
    var id: Long = 0
    lateinit var titulo: String
    lateinit var descricao: String
    lateinit var temporada: ToOne<Temporada>
}

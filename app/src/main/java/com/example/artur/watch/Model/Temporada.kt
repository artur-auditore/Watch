package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class Temporada {

    @Id
    var id: Long = 0
    var numero: Int = 0
    lateinit var titulo: String
    lateinit var descricao: String
    lateinit var serie: ToOne<Serie>
    lateinit var capitulos: List<Capitulo>
}

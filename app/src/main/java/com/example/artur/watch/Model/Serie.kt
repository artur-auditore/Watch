package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class Serie {

    @Id var id: Long = 0
    lateinit var titulo: String
    lateinit var genero: String
    var ano: Int = 0
    lateinit var sinopse: String
    lateinit var estudio: String
    lateinit var estrelando: String
    lateinit var tipo: String
    lateinit var usuario: ToOne<Usuario>

    override fun toString(): String {
        return "$titulo - $ano"
    }
}
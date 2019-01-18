package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Temporada(var numero: Int) {

    var capitulos = listOf<Capitulo>()
    lateinit var comentario: String

    @Id var id: Long = 0
}
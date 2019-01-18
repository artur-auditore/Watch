package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Serie : Filme(){

    var temporadas = listOf<Temporada>()
    @Id
    override var id: Long = 0
}
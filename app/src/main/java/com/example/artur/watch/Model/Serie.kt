package com.example.artur.watch.Model

import io.objectbox.annotation.Entity

@Entity
class Serie : Filme(){

    var temporadas = listOf<Temporada>()
}
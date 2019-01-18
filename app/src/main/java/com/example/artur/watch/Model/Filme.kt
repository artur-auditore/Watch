package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
open class Filme (var titulo: String,
                  var genero: String,
                  var ano: Int){

    constructor() : this("", "", 0)

    lateinit var estrelando: String
    lateinit var comentariosPessoais: String
    @Id
    open var id: Long = 0
}
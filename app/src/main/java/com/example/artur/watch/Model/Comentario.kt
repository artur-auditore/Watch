package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
class Comentario(var descricao: String,
                 var data: Date){

    var respostas = listOf<Comentario>()

    @Id
    var id: Long = 0
}
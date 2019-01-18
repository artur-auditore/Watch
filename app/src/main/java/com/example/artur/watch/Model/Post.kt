package com.example.artur.watch.Model

import io.objectbox.annotation.Id
import java.util.*

class Post (var descricao: String,
            var data: Date) {

    var comentario = listOf<Comentario>()

    @Id
    var id: Long = 0
}
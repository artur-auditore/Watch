package com.example.artur.watch.Model

import io.objectbox.annotation.Id

class Capitulo (var numero: Int, var comentario: String) {

    @Id var id: Long = 0
}
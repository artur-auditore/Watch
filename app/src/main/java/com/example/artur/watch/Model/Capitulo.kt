package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Capitulo (var numero: Int, var comentario: String) {

    @Id var id: Long = 0
}
package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

@Entity
class Comentario {

    @Id var id: Long = 0
    lateinit var descricao: String
    lateinit var data: Date
    lateinit var post: ToOne<Post>
    lateinit var usuario: ToOne<Usuario>
}
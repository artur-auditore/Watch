package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class Usuario (var nome: String,
                    var username: String,
                    var email: String,
                    var senha: String){

    var post = listOf<Post>()
    @Id var id: Long = 0
}
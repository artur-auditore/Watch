package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Usuario (var nome: String,
                    var username: String,
                    var email: String,
                    var senha: String){

    @Id var id: Long = 0
}
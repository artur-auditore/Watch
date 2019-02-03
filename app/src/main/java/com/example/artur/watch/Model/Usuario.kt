package com.example.artur.watch.Model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
class Usuario{

    @Id
    var id: Long = 0
    lateinit var nome: String
    @Unique lateinit var username: String
    lateinit var email: String
    lateinit var senha: String

    constructor()

    constructor(nome: String, username: String, email: String, senha: String) {
        this.nome = nome
        this.username = username
        this.email = email
        this.senha = senha
    }

    fun salvar(post: Post){

    }
}

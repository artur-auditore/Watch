package com.example.artur.watch

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.Model.Usuario_
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import com.example.artur.watch.Util.K.Companion.ID_USUARIO


class LoginActivity : AppCompatActivity() {

    private lateinit var editUsername: EditText
    private lateinit var editSenha: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textCadastro: TextView

    private lateinit var usuarioBox: Box<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        bind()
        cadastrar()
        login()
    }

    private fun bind(){

        editUsername = edit_username_login
        editSenha = edit_senha_login
        buttonLogin = button_login
        textCadastro = text_cadastrar
    }

    private fun login(){
        buttonLogin.setOnClickListener {
            val username = editUsername.text.toString()
            val senha = editSenha.text.toString()

            val result = usuarioBox.query()
                .equal(Usuario_.username, username)
                .equal(Usuario_.senha, senha)
                .build()
                .find()

            if (result.size > 0) {
                logar(result[0])
            } else {
                editSenha.text.clear()
                Toast.makeText(this, "Email e/ou senha incorreto(s)!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logar(usuario: Usuario){

        val sharedPreferences = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(ID_USUARIO, usuario.id)
        editor.apply()
        startActivity(Intent(this, TimeLineActivity::class.java))
    }

    private fun cadastrar(){
        textCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }
}

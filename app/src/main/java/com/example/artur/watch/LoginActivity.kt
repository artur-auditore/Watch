package com.example.artur.watch

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        const val KEY = "idUsuario"
    }

    private lateinit var editEmail: EditText
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
    }

    private fun cadastrar(){
        textCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun bind(){
        editEmail  =edit_email_login
        editSenha = edit_senha_login
        buttonLogin = button_login
        textCadastro = text_cadastrar
    }
}

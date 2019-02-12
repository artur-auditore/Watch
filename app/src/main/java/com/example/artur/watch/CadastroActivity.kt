package com.example.artur.watch

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.Model.Usuario_
import com.example.artur.watch.Util.K
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var editNome: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editSenha: EditText
    private lateinit var editConfirmSenha: EditText

    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        bind()

        val idUsuario = intent.getLongExtra(K.ID_USUARIO, K.DEFAULT_VALUE)
        if (idUsuario != K.DEFAULT_VALUE){
            supportActionBar!!.title = getString(R.string.suas_informacoes)
            usuario = usuarioBox.get(idUsuario)
            preecherDados(usuario)
        }
    }

    private fun preecherDados(usuario: Usuario){

        editNome.setText(usuario.nome)
        editUsername.setText(usuario.username)
        editEmail.setText(usuario.email)
        editSenha.setText(usuario.senha)
        editConfirmSenha.setText(usuario.senha)
    }

    private fun bind(){

        editNome = edit_nome
        editEmail = edit_email
        editUsername = edit_username
        editSenha = edit_senha
        editConfirmSenha = edit_confirm_senha

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)

        usuario = Usuario()
    }

    private fun logar(usuario: Usuario){

        val sharedPreferences = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(K.ID_USUARIO, usuario.id)
        editor.apply()
        startActivity(Intent(this, TimeLineActivity::class.java))
        Toast.makeText(this, getString(R.string.toast_logado), Toast.LENGTH_LONG).show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) { R.id.op_salvar -> cadastrar() }
        return super.onOptionsItemSelected(item)
    }

    private fun cadastrar(){

        val nome = editNome.text.toString()
        val username = editUsername.text.toString()
        val email = editEmail.text.toString()
        val senha = editSenha.text.toString()
        val confirmSenha = editConfirmSenha.text.toString()

        val result = usuarioBox.query()
            .equal(Usuario_.email, email)
            .build()
            .find()

        if (senha != confirmSenha){

            Toast.makeText(this, getString(R.string.erro_senhas_iguais), Toast.LENGTH_LONG).show()

        } else if (nome.trim() == "" || username.trim() == "" || email.trim() == "" ||
            senha.trim() == "" || confirmSenha.trim() == ""){

            Toast.makeText(this, getString(R.string.erro_preencher), Toast.LENGTH_LONG).show()

        } else if (result.size > 0) {

            Toast.makeText(this, getString(R.string.erro_email), Toast.LENGTH_LONG).show()

        } else {

            usuario.nome = nome
            usuario.username = username
            usuario.email = email
            usuario.senha = senha
            usuarioBox.put(usuario)
            logar(usuario)
            finish()
        }
    }
}

package com.example.artur.watch

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.artur.watch.Model.Filme
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_item.*

class FormularioItemActivity : AppCompatActivity() {

    companion object {
        const val ID = "idFilme"
        const val KEY = "idUsuario"
        const val DEFAULT_VALUE: Long = -1
    }

    private lateinit var editTitulo: EditText
    private lateinit var editGenero: EditText
    private lateinit var editAno: EditText
    private lateinit var editEstrelando: EditText
    private lateinit var editSinopse: EditText

    private lateinit var filmeBox: Box<Filme>
    private lateinit var serieBox: Box<Serie>
    private lateinit var filme: Filme
    private lateinit var serie: Serie

    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuarioLogado: Usuario

    private lateinit var radioButtonSim: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_item)

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        usuarioLogado = obterUsuario()
        filmeBox = ObjectBox.boxStore.boxFor(Filme::class.java)
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)

        bind()

        //Para editar
        val id = intent.getLongExtra(ID, DEFAULT_VALUE)
        if (id != DEFAULT_VALUE){
            //TODO completar depois
        }

    }

    fun bind(){
        editTitulo = edit_titulo
        editGenero = edit_genero
        editAno = edit_ano_lancamento
        editEstrelando = edit_estrelando
        editSinopse = edit_sinopse
        radioButtonSim = radio_sim

        serie = Serie()
        filme = Filme()

    }

    private fun obterUsuario(): Usuario {
        val pref = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar_filme, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){ R.id.salvar_filme -> salvarFilme() }
        return super.onOptionsItemSelected(item)
    }

    private fun salvarFilme(){
        val titulo = editTitulo.text.toString()
        val genero = editGenero.text.toString()
        val ano = editAno.text.toString()
        val estrelando = editEstrelando.text.toString()
        val sinopse = editSinopse.text.toString()

        if (titulo.trim() == "" || genero.trim() == "" || ano.trim() == ""){

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Aviso")
                .setMessage("Título, gênero e ano são campos obrigatórios!")
                .setNegativeButton("OK"){_, _ ->}
                .create()
                .show()

        } else {

            if (radioButtonSim.isChecked){

                serie.titulo = titulo
                serie.genero = genero
                serie.ano = ano.toInt()
                serie.estrelando = estrelando
                serie.sinopse = sinopse
                serie.usuario.target = usuarioLogado
                serieBox.put(serie)
                finish()

            } else {

                filme.titulo = titulo
                filme.genero = genero
                filme.ano = ano.toInt()
                filme.estrelando = estrelando
                filme.sinopse = sinopse
                filme.usuario.target = usuarioLogado
                filmeBox.put(filme)
                finish()

            }
        }


    }
}

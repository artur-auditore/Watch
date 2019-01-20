package com.example.artur.watch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.artur.watch.Model.Filme
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_item.*

class FormularioItemActivity : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editGenero: EditText
    private lateinit var editAno: EditText
    private lateinit var editEstrelando: EditText
    private lateinit var editSinopse: EditText

    private lateinit var filmeBox: Box<Filme>
    private lateinit var serieBox: Box<Serie>
    private lateinit var filme: Filme
    private lateinit var serie: Serie

    private lateinit var radioButtonSim: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_item)

        bind()
    }

    fun bind(){
        editTitulo = edit_titulo
        editGenero = edit_genero
        editAno = edit_ano_lancamento
        editEstrelando = edit_estrelando
        editSinopse = edit_sinopse
        radioButtonSim = radio_sim

        filmeBox = ObjectBox.boxStore.boxFor(Filme::class.java)
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
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

            Toast.makeText(this, "Título, gênero ou ano vazios", Toast.LENGTH_LONG).show()

        } else {

            if (radioButtonSim.isChecked){

                serie = Serie(titulo, genero, ano.toInt(), estrelando, sinopse)
                serieBox.put(serie)
                finish()
            } else {

                filme = Filme(titulo, genero, ano.toInt(), estrelando, sinopse)
                filmeBox.put(filme)
                finish()
            }
        }


    }
}

package com.example.artur.watch

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.artur.watch.Model.*
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_item.*

class FormularioSerieActivity : AppCompatActivity() {

    companion object {
        const val ID_SERIE = "idSerie"
        const val KEY = "idUsuario"
        const val DEFAULT_VALUE: Long = -1
    }

    private lateinit var editTitulo: EditText
    private lateinit var editGenero: EditText
    private lateinit var editAno: EditText
    private lateinit var editEstrelando: EditText
    private lateinit var editEstudio: EditText
    private lateinit var editQtdTemp: EditText
    private lateinit var editSinopse: EditText

    private lateinit var serieBox: Box<Serie>
    private lateinit var serie: Serie

    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuarioLogado: Usuario

    private lateinit var radioFilme: RadioButton
    private lateinit var radioSerie: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_item)

        bind()

        //Para editar
        val idSerie = intent.getLongExtra(ID_SERIE, DEFAULT_VALUE)
        if (idSerie != DEFAULT_VALUE){
            supportActionBar!!.title = "Editar Série"
            serie = serieBox.get(idSerie)
            preecherSerie(serie)
        }

    }

    private fun preecherSerie(serie: Serie){

        editTitulo.setText(serie.titulo)
        editGenero.setText(serie.genero)
        editAno.setText(serie.ano.toString())
        editEstrelando.setText(serie.estrelando)
        editEstudio.setText(serie.estudio)
        editSinopse.setText(serie.sinopse)
    }

    private fun bind(){

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        usuarioLogado = obterUsuario()
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)

        editTitulo = edit_titulo
        editGenero = edit_genero
        editAno = edit_ano_lancamento
        editEstudio = edit_estudio
        editEstrelando = edit_estrelando
        editSinopse = edit_sinopse
        radioFilme = tipo_filme
        radioSerie = tipo_serie

        serie = Serie()
    }

    private fun obterUsuario(): Usuario {

        val pref = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar_filme, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) { R.id.salvar_filme -> salvarSerie() }
        return super.onOptionsItemSelected(item)
    }

    private fun salvarSerie(){

        val titulo = editTitulo.text.toString()
        val genero = editGenero.text.toString()
        val ano = editAno.text.toString()
        val estrelando = editEstrelando.text.toString()
        val estudio = editEstudio.text.toString()
        val sinopse = editSinopse.text.toString()

        if (titulo.trim() == "" || genero.trim() == "" || ano.trim() == ""){

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Aviso")
                .setMessage("Título, gênero e ano são campos obrigatórios!")
                .setNegativeButton("OK"){_, _ ->}
                .create()
                .show()

        } else {

            if (radioFilme.isChecked) serie.tipo = radioFilme.text.toString()
            else serie.tipo = radioSerie.text.toString()

            serie.titulo = titulo
            serie.genero = genero
            serie.ano = ano.toInt()
            serie.estrelando = estrelando
            serie.estudio = estudio
            serie.sinopse = sinopse
            serie.usuario.target = usuarioLogado
            serieBox.put(serie)
            finish()

            Toast.makeText(
                this,
                "Salvo!", Toast.LENGTH_LONG
            ).show()

        }
    }
}


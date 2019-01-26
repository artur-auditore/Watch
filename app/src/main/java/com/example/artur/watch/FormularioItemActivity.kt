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
        const val ID_FILME = "idFilme"
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

    private lateinit var filmeBox: Box<Filme>
    private lateinit var serieBox: Box<Serie>
    private lateinit var filme: Filme
    private lateinit var serie: Serie

    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuarioLogado: Usuario

    private lateinit var radioButtonSim: RadioButton
    private lateinit var radioButtonNao: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_item)

        bind()

        //Para editar
        val idFilme = intent.getLongExtra(ID_FILME, DEFAULT_VALUE)
        val idSerie = intent.getLongExtra(ID_SERIE, DEFAULT_VALUE)

        if (idFilme != DEFAULT_VALUE){
            supportActionBar!!.title = "Editar Filme"
            filme = filmeBox.get(idFilme)
            preecherFilme(filme)
        }

        if (idSerie != DEFAULT_VALUE){
            supportActionBar!!.title = "Editar Série"
            serie = serieBox.get(idSerie)
            preecherSerie(serie)
        }

    }

    private fun preecherFilme(filme: Filme){

        editTitulo.setText(filme.titulo)
        editGenero.setText(filme.genero)
        editAno.setText(filme.ano.toString())
        editEstrelando.setText(filme.estrelando)
        editEstudio.setText(filme.estudio)
        editSinopse.setText(filme.sinopse)
        radioButtonNao.isChecked
    }

    private fun preecherSerie(serie: Serie){

        editTitulo.setText(serie.filme.target.titulo)
        editGenero.setText(serie.filme.target.genero)
        editAno.setText(serie.filme.target.ano.toString())
        editEstrelando.setText(serie.filme.target.estrelando)
        editEstudio.setText(serie.filme.target.estudio)
        editSinopse.setText(serie.filme.target.sinopse)
        radioButtonSim.isChecked
        editQtdTemp.setText(serie.qtdTemporadas.toString())
    }

    private fun bind(){

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        usuarioLogado = obterUsuario()
        filmeBox = ObjectBox.boxStore.boxFor(Filme::class.java)
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)

        editTitulo = edit_titulo
        editGenero = edit_genero
        editAno = edit_ano_lancamento
        editEstudio = edit_estudio
        editEstrelando = edit_estrelando
        editSinopse = edit_sinopse
        editQtdTemp = edit_qtd_temporadas
        radioButtonSim = radio_sim
        radioButtonNao = radio_nao

        serie = Serie()
        filme = Filme()
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
        when (item!!.itemId) { R.id.salvar_filme -> salvarFilme() }
        return super.onOptionsItemSelected(item)
    }

    private fun salvarFilme(){

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

            if (radioButtonSim.isChecked){

                editQtdTemp.visibility = View.VISIBLE
                val qtdTemp = editQtdTemp.text.toString()

                if (qtdTemp.trim() == ""){

                    Toast.makeText(this,
                        "Você deve adicionar a quantidade de temporadas", Toast.LENGTH_LONG).show()
                } else{

                    serie.filme.target = filme
                    serie.filme.target.titulo = titulo
                    serie.filme.target.genero = genero
                    serie.filme.target.ano = ano.toInt()
                    serie.filme.target.estrelando = estrelando
                    serie.filme.target.estudio = estudio
                    serie.filme.target.sinopse = sinopse
                    serie.usuario.target = usuarioLogado
                    serieBox.put(serie)
                    finish()

                    Toast.makeText(this,
                        "Série salva!", Toast.LENGTH_LONG).show()

                }

            } else {

                filme.titulo = titulo
                filme.genero = genero
                filme.ano = ano.toInt()
                filme.estrelando = estrelando
                filme.estudio = estudio
                filme.sinopse = sinopse
                filme.usuario.target = usuarioLogado
                filmeBox.put(filme)
                finish()

                Toast.makeText(this,
                    "Filme salvo!", Toast.LENGTH_LONG).show()

            }
        }
    }
}

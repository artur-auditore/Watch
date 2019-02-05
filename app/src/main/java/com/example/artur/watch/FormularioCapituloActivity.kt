package com.example.artur.watch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_capitulo.*

class FormularioCapituloActivity : AppCompatActivity() {

    companion object {
        const val ID_SERIE = "idSerie"
        const val ID_CAPITULO = "idCapitulo"
        const val DEFAULT_VALUE: Long = -1
    }

    private lateinit var editTitulo: EditText
    private lateinit var editDescricao: EditText
    private lateinit var editNCapitulo: EditText
    private lateinit var editNTemporada: EditText

    private lateinit var capituloBox: Box<Capitulo>
    private lateinit var capitulo: Capitulo

    private lateinit var serieBox: Box<Serie>
    private lateinit var serie: Serie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_capitulo)


        bind()

        val id = intent.getLongExtra(ID_CAPITULO, DEFAULT_VALUE)
        if (id != DEFAULT_VALUE){
            capitulo = capituloBox.get(id)
            preecherCapitulo(capitulo)
        }
    }

    private fun preecherCapitulo(capitulo: Capitulo) {

        editTitulo.setText(capitulo.titulo)
        editDescricao.setText(capitulo.descricao)
        editNTemporada.setText(capitulo.nTemporada)
        editNCapitulo.setText(capitulo.nCapitulo)
    }

    private fun bind(){

        editTitulo = edit_titulo_capitulo
        editDescricao = edit_capitulo_descricao
        editNCapitulo = edit_n_capitulo
        editNTemporada = edit_n_temporada

        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        serie = serieBox.get(intent.getLongExtra(ID_SERIE, DEFAULT_VALUE))

        capituloBox = ObjectBox.boxStore.boxFor(Capitulo::class.java)
        capitulo = Capitulo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) { R.id.op_salvar -> salvarCapitulo() }
        return super.onOptionsItemSelected(item)
    }

    private fun salvarCapitulo(){

        val titulo = editTitulo.text.toString()
        val descricao = editDescricao.text.toString()
        val nTemporada = editNTemporada.text.toString()
        val nCapitulo = editNCapitulo.text.toString()

        if (titulo.trim() == "" || nTemporada.trim() == "" || nCapitulo.trim() == ""){

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Aviso")
                .setMessage("Título é um campo obrigatório!")
                .setNegativeButton("OK"){_, _ ->}
                .create()
                .show()

        } else {

            capitulo.titulo = titulo
            capitulo.descricao = descricao
            capitulo.nCapitulo = nCapitulo.toInt()
            capitulo.nTemporada = nTemporada.toInt()
            capitulo.serie.target = serie
            capituloBox.put(capitulo)
            finish()

            Toast.makeText(this, "Capítulo salvo", Toast.LENGTH_LONG).show()
        }
    }
}

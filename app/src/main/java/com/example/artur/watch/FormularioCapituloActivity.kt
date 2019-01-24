package com.example.artur.watch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.Model.Temporada
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_capitulo.*

class FormularioCapituloActivity : AppCompatActivity() {

    companion object {
        const val ID = "idTemporada"
    }

    private lateinit var editTitulo: EditText
    private lateinit var editDescricao: EditText

    private lateinit var capituloBox: Box<Capitulo>
    private lateinit var temporadaBox: Box<Temporada>
    private lateinit var temporadaAtual: Temporada
    private lateinit var capitulo: Capitulo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_capitulo)

        bind()
    }

    private fun bind(){
        editTitulo = edit_titulo_capitulo
        editDescricao = edit_capitulo_descricao
        temporadaBox = ObjectBox.boxStore.boxFor(Temporada::class.java)
        capituloBox = ObjectBox.boxStore.boxFor(Capitulo::class.java)
        temporadaAtual = temporadaBox.get(intent.getLongExtra(ID, 0))
        capitulo = Capitulo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) { R.id.op_salvar -> salvarCapitulo()}
        return super.onOptionsItemSelected(item)
    }

    private fun salvarCapitulo(){

        val titulo = editTitulo.text.toString()
        val descricao = editDescricao.text.toString()

        if (titulo.trim() == ""){

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Aviso")
                .setMessage("Título é um campo obrigatório!")
                .setNegativeButton("OK"){_, _ ->}
                .create()
                .show()

        } else {

            capitulo.titulo = titulo
            capitulo.descricao = descricao
            capitulo.temporada.target = temporadaAtual
            capituloBox.put(capitulo)
            finish()

            Toast.makeText(this, "Capítulo salvo", Toast.LENGTH_LONG).show()
        }
    }
}

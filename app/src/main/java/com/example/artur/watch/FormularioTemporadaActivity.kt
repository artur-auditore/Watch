package com.example.artur.watch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.Model.Temporada
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_temporada.*

class FormularioTemporadaActivity : AppCompatActivity() {

    companion object {
        const val ID = "idSerie"
    }

    private lateinit var editNTemp: EditText
    private lateinit var editTitulo: EditText
    private lateinit var editDesc: EditText

    private lateinit var serieBox: Box<Serie>
    private lateinit var serieDona: Serie
    private lateinit var temporadaBox: Box<Temporada>
    private lateinit var temporada: Temporada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_temporada)

        bind()

        //Para editar
    }

    private fun bind(){
        editNTemp = edit_numero_temp
        editTitulo = edit_titulo_temp
        editDesc = edit_descricao

        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        serieDona = serieBox.get(intent.getLongExtra(ID, -1))
        temporadaBox = ObjectBox.boxStore.boxFor(Temporada::class.java)

        temporada = Temporada()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){ R.id.op_cadastar -> salvar() }
        return super.onOptionsItemSelected(item)
    }

    private fun salvar(){

        val nTemp = editNTemp.text.toString()
        val titulo = editTitulo.text.toString()
        val descricao = editDesc.text.toString()

        if (nTemp.trim() == ""){
            Toast.makeText(this, "O número da temporada é obrigatório",
                Toast.LENGTH_LONG).show()
        } else {

            temporada.numero = nTemp.toInt()
            temporada.titulo = titulo
            temporada.descricao = descricao
            temporada.serie.target = serieDona

            temporadaBox.put(temporada)
            Toast.makeText(this, "Salvo!", Toast.LENGTH_LONG).show()
            finish()
        }

    }
}

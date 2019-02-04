package com.example.artur.watch

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.artur.watch.Adapter.CapituloAdapter
import com.example.artur.watch.Adapter.TemporadaAdapter
import com.example.artur.watch.Model.*
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_lista_temporadas.*

@SuppressLint("Registered")
class InfoSerieActivity : AppCompatActivity() {

    companion object {
        const val ID_SERIE = "idSerie"
        const val DEFAUT_VALUE: Long = -1
    }

    private lateinit var fabNewTemp: FloatingActionButton
    private lateinit var temporadaBox: Box<Temporada>
    private lateinit var serieBox: Box<Serie>
    private lateinit var serieAtual: Serie
    private lateinit var recyclerView: RecyclerView

    private lateinit var textTituloSerie: TextView
    private lateinit var textGeneroSerie: TextView
    private lateinit var textAnoSerie: TextView
    private lateinit var textEstudioSerie: TextView

    private lateinit var adapter: TemporadaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_temporadas)

        bind()

        novaTemporada()

    }

    private fun novaTemporada(){
        fabNewTemp.setOnClickListener {

            val intent = Intent(this, FormularioTemporadaActivity::class.java)
            intent.putExtra(ID_SERIE, serieAtual.id)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bind(){

        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        serieAtual = serieBox.get(intent.getLongExtra(ID_SERIE, 0))
        temporadaBox = ObjectBox.boxStore.boxFor(Temporada::class.java)
        fabNewTemp = fab_nova_temporada
        recyclerView = rv_temporadas

        textTituloSerie = text_titulo_serie_filme
        textGeneroSerie = text_genero_serie_filme
        textAnoSerie = text_ano_serie_filme
        textEstudioSerie = text_estudio_serie

        textTituloSerie.text = serieAtual.titulo
        textGeneroSerie.text = serieAtual.genero
        textAnoSerie.text = serieAtual.ano.toString()
        textEstudioSerie.text = "Série Original ${serieAtual.estudio}"

    }

    private fun loadTemporadas(list: MutableList<Temporada>){

        adapter = TemporadaAdapter(this, list, temporadaBox)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_temporadas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.op_excluir_tudo -> excluirTemporadas()
            R.id.op_excluir_serie -> excluirSerie()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun excluirSerie(){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir Série")
            .setMessage("Deseja realemnte excluir ${serieAtual.titulo} da sua lista de séries?" +
                    "Todo seu esquema desta série será apagado e esta ação não poderá ser desfeita")
            .setPositiveButton("Excluir"){_, _ ->
                serieBox.remove(serieAtual)
                Toast.makeText(this, "${serieAtual.titulo} apagado", Toast.LENGTH_LONG).show()
                finish()
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()
    }

    private fun excluirTemporadas(){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir tudo")
            .setMessage("Deseja realmente excluir todas as temporadas que você adicionou a ${serieAtual.titulo}? " +
                    "Os capítulos das temporadas serão apagados e esta ação não poderá ser desfeita.")
            .setPositiveButton("Excluir Tudo"){_, _ ->

                val list = temporadaBox.query()
                    .equal(Temporada_.serieId, serieAtual.id)
                    .build().find()

                temporadaBox.remove(list)
                Toast.makeText(this, "Tudo apagado", Toast.LENGTH_LONG).show()
                loadTemporadas(list)
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()

    }

    override fun onResume() {
        super.onResume()

        val list = temporadaBox.query()
            .equal(Temporada_.serieId, serieAtual.id)
            .build().find()

        loadTemporadas(list)
    }
}

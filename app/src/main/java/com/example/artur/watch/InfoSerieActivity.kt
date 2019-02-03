package com.example.artur.watch

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.TextView
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
        serieAtual = serieBox.get(intent.getLongExtra(ID_SERIE, DEFAUT_VALUE))
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

        recyclerView.adapter = TemporadaAdapter(this, list, temporadaBox)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onResume() {
        super.onResume()

        val list = temporadaBox.query()
            .equal(Temporada_.serieId, serieAtual.id)
            .build().find()

        loadTemporadas(list)
    }
}

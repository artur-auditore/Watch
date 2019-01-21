package com.example.artur.watch

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.Toast
import com.example.artur.watch.Adapter.TemporadaAdapter
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.Model.Temporada
import com.example.artur.watch.Model.Temporada_
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_lista_temporadas.*

@SuppressLint("Registered")
class ListaTemporadasActivity : AppCompatActivity() {

    companion object {
        const val ID = "idSerie"
    }

    private lateinit var fabNewTemp: FloatingActionButton
    private lateinit var temporadaBox: Box<Temporada>
    private lateinit var serieBox: Box<Serie>
    private lateinit var serieAtual: Serie
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_temporadas)

        bind()

        fabNewTemp.setOnClickListener {
            val intent = Intent(this, FormularioTemporadaActivity::class.java)
            intent.putExtra(ID, serieAtual.id)
            startActivity(intent)
        }
    }

    private fun bind(){
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        serieAtual = serieBox.get(intent.getLongExtra(ID, -1))
        temporadaBox = ObjectBox.boxStore.boxFor(Temporada::class.java)
        fabNewTemp = fab_nova_temporada
        recyclerView = rv_temporadas

    }

    private fun loadTemporadas(list: MutableList<Temporada>){

        recyclerView.adapter = TemporadaAdapter(this, list, temporadaBox)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.hasFixedSize()
    }

    override fun onResume() {
        super.onResume()

        val query = temporadaBox.query()
        val list = query.equal(Temporada_.serieId, serieAtual.id)
            .build().find()

        loadTemporadas(list)
    }
}
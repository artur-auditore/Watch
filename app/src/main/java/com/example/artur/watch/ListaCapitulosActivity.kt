package com.example.artur.watch

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.artur.watch.Adapter.CapituloAdapter
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.Model.Capitulo_
import com.example.artur.watch.Model.Temporada
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_lista_capitulos.*

class ListaCapitulosActivity : AppCompatActivity() {

    companion object {
        const val ID = "idTemporada"
        const val DEFAULT_VAUE: Long = -1
        const val NUMERO_TEMPORADA: String = "numeroTemporada"

    }

    private lateinit var fabNovoCapitulo: FloatingActionButton
    private lateinit var capituloBox: Box<Capitulo>
    private lateinit var temporadaBox: Box<Temporada>
    private lateinit var recyclerView: RecyclerView
    private lateinit var temporadaAtual: Temporada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_capitulos)

        bind()

        supportActionBar!!.title = "Temporada ${temporadaAtual.numero}"

        novoCapitulo()
    }

    override fun onResume() {
        super.onResume()

        val list = capituloBox.query()
            .equal(Capitulo_.temporadaId, temporadaAtual.id)
            .build()
            .find()

        loadCapitulos(list)
    }

    private fun loadCapitulos(list: MutableList<Capitulo>){

        recyclerView.adapter = CapituloAdapter(this, list, capituloBox)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }

    private fun bind(){
        fabNovoCapitulo = fab_novo_capitulo
        capituloBox = ObjectBox.boxStore.boxFor(Capitulo::class.java)
        temporadaBox = ObjectBox.boxStore.boxFor(Temporada::class.java)
        recyclerView = rv_capitulos

        temporadaAtual = temporadaBox.get(intent.getLongExtra(ID, DEFAULT_VAUE))
    }

    private fun novoCapitulo(){
        fabNovoCapitulo.setOnClickListener {
            val intent = Intent(this, FormularioCapituloActivity::class.java)
            intent.putExtra(ID, getIntent().getLongExtra(ID, 0))
            startActivity(intent)
        }
    }
}

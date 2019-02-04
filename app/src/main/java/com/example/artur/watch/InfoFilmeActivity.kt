package com.example.artur.watch

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.artur.watch.Adapter.CapituloAdapter
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.Model.Capitulo_
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_info_filme.*
import kotlinx.android.synthetic.main.view_dialog_nova_nota.view.*

class InfoFilmeActivity : AppCompatActivity() {

    companion object {
        const val ID_FILME = "idFilme"
    }

    private lateinit var textTitulo: TextView
    private lateinit var textGenero: TextView
    private lateinit var textAno: TextView
    private lateinit var textEstudio: TextView
    private lateinit var fabNovaNota: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var capituloBox: Box<Capitulo>
    private lateinit var serieBox: Box<Serie>

    private lateinit var serie: Serie
    private lateinit var capitulo: Capitulo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_filme)

        bind()

        novaNota()
    }

    @SuppressLint("InflateParams")
    private fun novaNota(){
        fabNovaNota.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val viewDialog = layoutInflater.inflate(R.layout.view_dialog_nova_nota, null)

            val editTitulo = viewDialog.edit_titulo_nota
            val descricao = viewDialog.edit_descricao_nota

            alertDialog.setView(viewDialog)
                .setTitle("Nova Nota")
                .setPositiveButton("Salvar"){_, _ ->

                    capitulo.titulo = editTitulo.text.toString()
                    capitulo.descricao = descricao.text.toString()
                    capitulo.serie.target = serie
                    capituloBox.put(capitulo)

                    Snackbar.make(viewDialog,
                        "Adicionado",
                        Snackbar.LENGTH_LONG).show()

                    loadCapitulos()

                }
                .setNegativeButton("Cancelar"){_, _ ->}
                .create().show()
        }
    }

    override fun onResume() {
        super.onResume()

        loadCapitulos()
    }

    private fun loadCapitulos(){

        val list = capituloBox.query()
            .equal(Capitulo_.serieId, serie.id)
            .build().find()

        recyclerView.adapter = CapituloAdapter(this, list, capituloBox)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    @SuppressLint("SetTextI18n")
    private fun bind(){

        fabNovaNota = fab_nova_nota
        recyclerView = rv_notas
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        serie = serieBox.get(intent.getLongExtra(ID_FILME, 0))
        capituloBox = ObjectBox.boxStore.boxFor(Capitulo::class.java)
        capitulo = Capitulo()

        textTitulo = text_titulo_filme
        textGenero = text_genero_filme
        textAno = text_ano_filme
        textEstudio = text_estudio_filme

        textTitulo.text = serie.titulo
        textGenero.text = serie.genero
        textAno.text = serie.ano.toString()
        textEstudio.text = "Produzido por: ${serie.estudio}"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filme, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.op_excluir_filme -> excluirFilme()
            R.id.op_excluir_capitulos -> excluirCapitulos()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun excluirFilme(){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir Filme")
            .setMessage("Deseja realmente excluir ${serie.titulo} da sua lista de filmes? " +
                    "Esta ação não poderá ser desfeita.")
            .setPositiveButton("Excluir"){_, _ ->
                serieBox.remove(serie)
                Toast.makeText(this, "${serie.titulo} apagado", Toast.LENGTH_LONG).show()
                finish()
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()
    }

    private fun excluirCapitulos(){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir Tudo")
            .setMessage("Deseja realmente excluir todas as notas que você fez de ${serie.titulo}? " +
                    "Esta ação não poderá ser desfeita.")
            .setPositiveButton("Excluir"){_, _ ->

                val list = capituloBox.query()
                    .equal(Capitulo_.serieId, serie.id)
                    .build().find()

                capituloBox.remove(list)
                Toast.makeText(this, "Tudo apagado", Toast.LENGTH_LONG).show()
                finish()
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()
    }
}

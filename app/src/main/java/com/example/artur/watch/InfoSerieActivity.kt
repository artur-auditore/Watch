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
import com.example.artur.watch.Model.*
import com.example.artur.watch.Util.K.Companion.DEFAULT_VALUE
import com.example.artur.watch.Util.K.Companion.ID_SERIE
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_capitulo.view.*
import kotlinx.android.synthetic.main.activity_lista_temporadas.*

@SuppressLint("Registered")
class InfoSerieActivity : AppCompatActivity() {

    private lateinit var fabNovoCapitulo: FloatingActionButton
    private lateinit var serieBox: Box<Serie>
    private lateinit var serieAtual: Serie
    private lateinit var recyclerView: RecyclerView

    private lateinit var textTituloSerie: TextView
    private lateinit var textGeneroSerie: TextView
    private lateinit var textAnoSerie: TextView
    private lateinit var textEstudioSerie: TextView

    private lateinit var capituloBox: Box<Capitulo>
    private lateinit var capitulo: Capitulo
    private lateinit var postBox: Box<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_temporadas)

        bind()

        fabNovoCapitulo.setOnClickListener {
            novoCapitulo()
        }
    }

    @SuppressLint("InflateParams")
    private fun novoCapitulo(){

        val alertDialog = AlertDialog.Builder(this)

        val viewDialog = layoutInflater.inflate(R.layout.activity_formulario_capitulo, null)

        val editTitulo = viewDialog.edit_titulo_capitulo
        val editNTemp = viewDialog.edit_n_temporada
        val editNCap = viewDialog.edit_n_capitulo
        val editDescricao = viewDialog.edit_capitulo_descricao

        alertDialog.setView(viewDialog)
            .setTitle("Novo Capítulo")
            .setPositiveButton("Salvar"){_, _ ->

                val titulo = editTitulo.text.toString()
                val nTemp = editNTemp.text.toString()
                val nCapitulo = editNCap.text.toString()
                val descricao = editDescricao.text.toString()

                if (titulo.trim() == "" || nTemp.trim() == "" || nCapitulo.trim() == ""){
                    Toast.makeText(
                        this,
                        getString(R.string.aviso_dialog_capitulo),
                        Toast.LENGTH_LONG
                    ).show()

                } else {

                    capitulo = Capitulo()
                    capitulo.titulo = titulo
                    capitulo.nCapitulo = nCapitulo.toInt()
                    capitulo.nTemporada = nTemp.toInt()
                    capitulo.descricao = descricao
                    capitulo.serie.target = serieAtual
                    capituloBox.put(capitulo)

                    Toast.makeText(this,
                        "Capítulo salvo",
                        Toast.LENGTH_LONG
                    ).show()
                }
                loadCapitulos()
            }
            .setNeutralButton("Cancelar"){_, _ ->}
            .create().show()
    }

    @SuppressLint("SetTextI18n")
    private fun bind(){

        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        serieAtual = serieBox.get(intent.getLongExtra(ID_SERIE, DEFAULT_VALUE))
        capituloBox = ObjectBox.boxStore.boxFor(Capitulo::class.java)
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)
        fabNovoCapitulo = fab_novo_capitulo
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

    private fun loadCapitulos(){

        val list = capituloBox.query()
            .equal(Capitulo_.serieId, serieAtual.id)
            .build().find()

        val adapter = CapituloAdapter(this, list, capituloBox)
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
            R.id.op_excluir_tudo -> excluirCapitulos()
            R.id.op_excluir_serie -> excluirSerie()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun excluirSerie(){

        val list = postBox.query()
            .equal(Post_.serieId, serieAtual.id).build().find()

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir Série")
            .setMessage(
                "Deseja realemnte excluir ${serieAtual.titulo} da sua lista de séries?" +
                    "Todo seu esquema desta série será apagado e esta ação não poderá ser desfeita"
            )
            .setPositiveButton("Excluir") { _, _ ->

                if (list.size > 0) {

                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Erro")
                        .setMessage(
                            "Não é possível excluir ${serieAtual.titulo} porque existem uma ou mais publicações" +
                                    " associadas. Apague a(s) publicação(ões) e tente novamente."
                        )
                        .setNegativeButton("Ok") { _, _ -> }.create().show()

                } else {

                    serieBox.remove(serieAtual)
                    Toast.makeText(this, "${serieAtual.titulo} apagado", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()
    }

    private fun excluirCapitulos(){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Excluir tudo")
            .setMessage("Deseja realmente excluir todas os capítulos que você adicionou a ${serieAtual.titulo}? " +
                    "Esta ação não poderá ser desfeita.")
            .setPositiveButton("Excluir Tudo"){_, _ ->

                val list = capituloBox.query()
                    .equal(Capitulo_.serieId, serieAtual.id)
                    .build().find()

                capituloBox.remove(list)
                Toast.makeText(this, "Tudo apagado", Toast.LENGTH_LONG).show()
                loadCapitulos()
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()

    }

    override fun onResume() {
        super.onResume()

        loadCapitulos()
    }
}

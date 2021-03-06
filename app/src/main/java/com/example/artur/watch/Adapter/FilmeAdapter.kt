package com.example.artur.watch.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.artur.watch.*
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Post_
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.Util.K.Companion.ID_FILME
import com.example.artur.watch.Util.K.Companion.ID_SERIE
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_filme.view.*

class FilmeAdapter(private val context: Context,
                   private val series: MutableList<Serie>,
                   private val serieBox: Box<Serie>): RecyclerView.Adapter<FilmeAdapter.ViewHolder>() {

    private val postbox = ObjectBox.boxStore.boxFor(Post::class.java)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val titulo = itemView.titulo_filme
        val genero = itemView.genero_filme
        val ano = itemView.ano_filme

        val imgCompartilhar = itemView.imageCompartilhar
        val opcoes = itemView.imageOpcoes

        fun bind(serie: Serie){

            titulo.text = serie.titulo
            genero.text = serie.genero
            ano.text = serie.ano.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filme, parent, false))
    }

    override fun getItemCount(): Int {
        return series.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serie = series[position]
        holder.bind(serie)

        holder.itemView.setOnClickListener {
            informacoes(serie, position)
        }

        holder.imgCompartilhar.setOnClickListener {
            compartilhar(serie, position)
        }

        holder.opcoes.setOnClickListener {
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_pop, popup.menu)

            popup.setOnMenuItemClickListener {item ->

                when(item.itemId){
                    R.id.op_editar -> editar(serie, position)
                    R.id.op_excluir -> excluir(holder.itemView, serie, position)
                }

                false
            }

            popup.show()
        }

        menuPop(holder.itemView, serie, position)
    }

    private fun informacoes(serie: Serie, position: Int){

        val intent = Intent(context, InfoFilmeActivity::class.java)
        intent.putExtra(ID_FILME, serie.id)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun compartilhar(serie: Serie, position: Int){

        val intent = Intent(context, FormularioPostActivity::class.java)
        intent.putExtra(ID_SERIE, serie.id)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun menuPop(itemView: View, serie: Serie, position: Int){

        itemView.setOnLongClickListener {it ->

            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_pop, popup.menu)

            popup.setOnMenuItemClickListener {item ->

                when(item.itemId){
                    R.id.op_editar -> editar(serie, position)
                    R.id.op_excluir -> excluir(itemView, serie, position)
                }

                false
            }

            popup.show()

            true
        }
    }

    private fun excluir(itemView: View, serie: Serie, position: Int) {

        val list = postbox.query().equal(Post_.serieId, serie.id)
            .build().find()

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir")
            .setMessage("Deseja realmente excluir ${serie.titulo} da sua lista de séries?")
            .setPositiveButton("SIM"){_, _ ->

                if (list.size > 0){

                    val alert = AlertDialog.Builder(context)
                    alert.setTitle("Erro")
                        .setMessage("Não é possível excluir ${serie.titulo} porque existem uma ou mais publicações" +
                                " associadas. Apague a(s) publicação(ões) e tente novamente.")
                        .setNegativeButton("Ok"){_, _ ->}.create().show()

                } else {

                    this.series.remove(serie)
                    this.serieBox.remove(serie)
                    notifyItemChanged(position)
                    notifyItemChanged(position, itemCount)
                    Snackbar.make(itemView, "${serie.titulo} apagado.", Snackbar.LENGTH_LONG).show()
                }


            }
            .setNegativeButton("Não"){_, _ ->}
            .create().show()
    }

    private fun editar(serie: Serie, position: Int) {

        val intent = Intent(context, FormularioSerieActivity::class.java)
        intent.putExtra(ID_SERIE, serie.id)
        context.startActivity(intent)
        notifyItemChanged(position)
    }
}
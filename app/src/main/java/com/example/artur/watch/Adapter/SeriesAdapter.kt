package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.artur.watch.FormularioItemActivity
import com.example.artur.watch.InfoSerieActivity
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_filme.view.*

class SeriesAdapter(private val context: Context,
                    private val series: MutableList<Serie>,
                    private val serieBox:Box<Serie>): RecyclerView.Adapter<SeriesAdapter.ViewHolder>() {

    companion object {
        const val ID = "idSerie"
        const val TIPO = "serie"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(serie: Serie){

            val titulo = itemView.titulo_filme
            val genero = itemView.genero_filme
            val ano = itemView.ano_filme

            titulo.text = serie.filme.target.titulo
            genero.text = serie.filme.target.genero
            ano.text = "${serie.filme.target.ano}"
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

            val intent = Intent(context, InfoSerieActivity::class.java)
            intent.putExtra(ID, serie.id)
            context.startActivity(intent)
            notifyItemChanged(position)
        }

        menuPop(holder.itemView, serie, position)
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

    private fun editar(serie: Serie, position: Int) {

        val intent = Intent(context, FormularioItemActivity::class.java)
        intent.putExtra(ID, serie.id)
        intent.putExtra("nome", TIPO)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun excluir(view: View, serie: Serie, position: Int){

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir")
            .setMessage("Deseja realmente excluir ${serie.filme.target.titulo} da sua lista de filmes?")
            .setPositiveButton("SIM"){_, _ ->

                this.series.remove(serie)
                this.serieBox.remove(serie)
                notifyItemChanged(position)
                notifyItemChanged(position, itemCount)
                Snackbar.make(view, "${serie.filme.target.titulo} apagado.", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Não"){_, _ ->}
            .create()
            .show()
    }
}
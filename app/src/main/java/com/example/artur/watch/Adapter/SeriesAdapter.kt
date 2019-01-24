package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artur.watch.ListaTemporadasActivity
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_filme.view.*

class SeriesAdapter(private val context: Context,
                    private val series: MutableList<Serie>,
                    private val serieBox:Box<Serie>): RecyclerView.Adapter<SeriesAdapter.ViewHolder>() {

    companion object {
        const val ID = "idSerie"
        const val NOME_SERIE = "nomeSerie"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(serie: Serie){

            val titulo = itemView.titulo_filme
            val genero = itemView.genero_filme
            val ano = itemView.ano_filme

            titulo.text = "Título: ${serie.titulo}"
            genero.text = "Gênero: ${serie.genero}"
            ano.text = "Ano: ${serie.ano}"
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

            val intent = Intent(context, ListaTemporadasActivity::class.java)
            intent.putExtra(ID, serie.id)
            intent.putExtra(NOME_SERIE, serie.titulo)
            context.startActivity(intent)
            notifyItemChanged(position)
        }
    }
}
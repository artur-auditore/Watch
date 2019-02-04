package com.example.artur.watch.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artur.watch.ListaCapitulosActivity
import com.example.artur.watch.Model.Temporada
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_temporada.view.*

class TemporadaAdapter(private val context: Context,
                       private val temporadas: MutableList<Temporada>,
                       private val temporadaBox: Box<Temporada>

): RecyclerView.Adapter<TemporadaAdapter.ViewHolder>() {

    companion object {
        const val ID = "idTemporada"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(temporada: Temporada){
            val nTemp = itemView.text_numero_temp
            val titulo = itemView.text_titulo_temp
            val descricao = itemView.text_desc_temp

            nTemp.text = temporada.numero.toString()
            titulo.text = temporada.titulo
            descricao.text = temporada.descricao
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_temporada, parent, false))
    }

    override fun getItemCount(): Int {
        return temporadas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temporada = temporadas[position]
        holder.bind(temporada)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, ListaCapitulosActivity::class.java)
            intent.putExtra(ID, temporada.id)
            context.startActivity(intent)
            notifyItemChanged(position)
        }
    }
}
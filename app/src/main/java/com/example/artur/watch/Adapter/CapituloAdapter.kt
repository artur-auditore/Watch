package com.example.artur.watch.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_capitulo.view.*

class CapituloAdapter(private val context: Context,
                      private val capitulos: MutableList<Capitulo>,
                      private val capituloBox: Box<Capitulo>): RecyclerView.Adapter<CapituloAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(capitulo: Capitulo){
            val titulo = itemView.text_titulo_capitulo
            val descricao = itemView.text_desc_capitulo

            titulo.text = capitulo.titulo
            descricao.text = capitulo.descricao
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_capitulo, parent, false))
    }

    override fun getItemCount(): Int {
        return capitulos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capitulo = capitulos[position]
        holder.bind(capitulo)
    }
}
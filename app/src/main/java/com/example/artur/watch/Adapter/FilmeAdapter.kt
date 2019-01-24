package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.support.constraint.solver.widgets.Analyzer.setPosition
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.artur.watch.Model.Filme
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_filme.view.*



class FilmeAdapter(private val context: Context,
                   private val filmes: MutableList<Filme>,
                   private val filmesBox: Box<Filme>): RecyclerView.Adapter<FilmeAdapter.ViewHolder>(){

    companion object {
        const val ID = "idFilme"
    }

    @SuppressLint("NewApi")
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        @SuppressLint("SetTextI18n")
        fun bind(filme: Filme) {
            val titulo = itemView.titulo_filme
            val genero = itemView.genero_filme
            val ano = itemView.ano_filme

            titulo.text = "Título: ${filme.titulo}"
            genero.text = "Gênero: ${filme.genero}"
            ano.text = "Ano: ${filme.ano}"
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filme, parent, false))
    }

    override fun getItemCount(): Int {
        return filmes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filme = filmes[position]
        holder.bind(filme)

    }
}
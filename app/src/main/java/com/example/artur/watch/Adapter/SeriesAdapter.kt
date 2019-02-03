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
import com.example.artur.watch.*
import com.example.artur.watch.Model.Serie
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_serie.view.*

class SeriesAdapter(private val context: Context,
                    private val series: MutableList<Serie>,
                    private val serieBox:Box<Serie>): RecyclerView.Adapter<SeriesAdapter.ViewHolder>() {

    companion object {
        const val ID_SERIE = "idSerie"
        const val ID_FILME = "idFilme"
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titulo = itemView.titulo_serie
        val genero = itemView.genero_serie
        val ano = itemView.ano_serie
        val image = itemView.imageSerie

        val imgCompartilhar = itemView.imageCompartilhar
        val imgOpcoes = itemView.imageOpcoes

        @SuppressLint("SetTextI18n")
        fun bind(serie: Serie){

            titulo.text = serie.titulo
            genero.text = serie.genero
            ano.text = "${serie.ano}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_serie, parent, false))
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

        holder.imgOpcoes.setOnClickListener {
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

    private fun compartilhar(serie: Serie, position: Int){

        val intent = Intent(context, FormularioPostActivity::class.java)
        intent.putExtra(ID_SERIE, serie.id)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun informacoes(serie: Serie, position: Int){

        val intentSerie = Intent(context, InfoSerieActivity::class.java)
        intentSerie.putExtra(ID_SERIE, serie.id)
        val intentFilme = Intent(context, InfoFilmeActivity::class.java)
        intentFilme.putExtra(ID_FILME, serie.id)

        if (serie.tipo == "Filme") context.startActivity(intentFilme)
        else context.startActivity(intentSerie)

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

    private fun editar(serie: Serie, position: Int) {

        val intent = Intent(context, FormularioSerieActivity::class.java)
        intent.putExtra(ID_SERIE, serie.id)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun excluir(view: View, serie: Serie, position: Int){

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir")
            .setMessage("Deseja realmente excluir ${serie.titulo} da sua lista de séries?")
            .setPositiveButton("SIM"){_, _ ->

                this.series.remove(serie)
                this.serieBox.remove(serie)
                notifyItemChanged(position)
                notifyItemChanged(position, itemCount)
                Snackbar.make(view, "${serie.titulo} apagado.", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Não"){_, _ ->}
            .create()
            .show()
    }
}
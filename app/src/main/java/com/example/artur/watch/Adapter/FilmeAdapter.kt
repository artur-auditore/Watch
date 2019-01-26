package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.constraint.solver.widgets.Analyzer.setPosition
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.PopupMenu
import com.example.artur.watch.FormularioItemActivity
import com.example.artur.watch.Model.Filme
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_filme.view.*



class FilmeAdapter(private val context: Context,
                   private val filmes: MutableList<Filme>,
                   private val filmesBox: Box<Filme>): RecyclerView.Adapter<FilmeAdapter.ViewHolder>(){

    companion object {
        const val ID = "idFilme"
        const val TIPO = "filme"
    }

    @SuppressLint("NewApi")
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        @SuppressLint("SetTextI18n")
        fun bind(filme: Filme) {
            val titulo = itemView.titulo_filme
            val genero = itemView.genero_filme
            val ano = itemView.ano_filme

            titulo.text = filme.titulo
            genero.text = filme.genero
            ano.text = "${filme.ano}"
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

        menuPop(holder.itemView, filme, position)

    }

    private fun menuPop(itemView: View, filme: Filme, position: Int){
        itemView.setOnLongClickListener {it ->

            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_pop, popup.menu)

            popup.setOnMenuItemClickListener {item ->

                when(item.itemId){
                    R.id.op_editar -> editar(filme, position)
                    R.id.op_excluir -> excluir(itemView, filme, position)
                }

                false
            }

            popup.show()

            true
        }
    }

    private fun editar(filme: Filme, position: Int) {

        val intent = Intent(context, FormularioItemActivity::class.java)
        intent.putExtra(ID, filme.id)
        intent.putExtra("nome", TIPO)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun excluir(view: View, filme: Filme, position: Int){

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir")
            .setMessage("Deseja realmente excluir ${filme.titulo} da sua lista de filmes?")
            .setPositiveButton("SIM"){_, _ ->

                this.filmes.remove(filme)
                this.filmesBox.remove(filme)
                notifyItemChanged(position)
                notifyItemChanged(position, itemCount)
                Snackbar.make(view, "${filme.titulo} apagado.", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("Não"){_, _ ->}
            .create()
            .show()
    }
}
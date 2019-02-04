package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_capitulo.view.*
import kotlinx.android.synthetic.main.view_dialog_nova_nota.view.*

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

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val capitulo = capitulos[position]
        holder.bind(capitulo)

        holder.itemView.setOnClickListener {
            verOuEditar(capitulo, position)
        }

        holder.itemView.setOnLongClickListener {
            excluir(capitulo, position)
            true
        }
    }

    private fun excluir(capitulo: Capitulo, position: Int){

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir")
            .setMessage("Deseja realmente excluir ${capitulo.titulo} desta lista? " +
                    "Não será possível recuperar depois desta ação.")
            .setPositiveButton("Excluir"){_, _ ->
                this.capitulos.remove(capitulo)
                this.capituloBox.remove(capitulo)
                notifyItemChanged(position)
                notifyItemRangeChanged(position, itemCount)
                Toast.makeText(context, "Apagado", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()

    }

    @SuppressLint("InflateParams")
    private fun verOuEditar(capitulo: Capitulo, position: Int){

        val alertDialog = AlertDialog.Builder(context)
        val viewDialog = LayoutInflater.from(context).inflate(R.layout.view_dialog_nova_nota, null)

        val editTitulo = viewDialog.edit_titulo_nota
        val descricao = viewDialog.edit_descricao_nota

        editTitulo.setText(capitulo.titulo)
        descricao.setText(capitulo.descricao)

        alertDialog.setView(viewDialog)
            .setTitle("Nova Nota")
            .setPositiveButton("Salvar"){_, _ ->

                capitulo.titulo = editTitulo.text.toString()
                capitulo.descricao = descricao.text.toString()

                Snackbar.make(viewDialog,
                    "Adicionado",
                    Snackbar.LENGTH_LONG).show()

                notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()
    }
}
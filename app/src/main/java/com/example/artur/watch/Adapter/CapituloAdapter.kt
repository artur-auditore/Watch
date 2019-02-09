package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.artur.watch.FormularioCapituloActivity
import com.example.artur.watch.FormularioPostActivity
import com.example.artur.watch.Model.Capitulo
import com.example.artur.watch.R
import com.example.artur.watch.Util.K.Companion.ID_CAPITULO
import com.example.artur.watch.Util.K.Companion.TEXT_CAPITULO
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_capitulo.view.*
import kotlinx.android.synthetic.main.item_capitulo.view.*
import kotlinx.android.synthetic.main.view_dialog_nova_nota.view.*

class CapituloAdapter(private val context: Context,
                      private val capitulos: MutableList<Capitulo>,
                      private val capituloBox: Box<Capitulo>): RecyclerView.Adapter<CapituloAdapter.ViewHolder>(){


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val titulo = itemView.text_titulo_capitulo
        val descricao = itemView.text_desc_capitulo
        val nTempNCap = itemView.text_n_temp_n_capitulo
        val imgCompartilhar = itemView.imageCompartilhar

        @SuppressLint("SetTextI18n")
        fun bind(capitulo: Capitulo){

            titulo.text = capitulo.titulo
            descricao.text = capitulo.descricao
            if (capitulo.serie.target.tipo == "Filme") nTempNCap.visibility = View.GONE
            else nTempNCap.text = "T${capitulo.nTemporada}: E${capitulo.nCapitulo}"

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

        holder.imgCompartilhar.setOnClickListener {
            val intent = Intent(context, FormularioPostActivity::class.java)
            intent.putExtra(ID_CAPITULO, capitulo.id)
            context.startActivity(intent)
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

    private fun verOuEditar(capitulo: Capitulo, position: Int){

        val alertDialog = AlertDialog.Builder(context)

        if (capitulo.serie.target.tipo == "Série"){
            verCapitulo(alertDialog, capitulo, position)

        } else {
            verNota(alertDialog, capitulo, position)
        }

    }

    @SuppressLint("InflateParams")
    private fun verCapitulo(alertDialog: AlertDialog.Builder, capitulo: Capitulo, position: Int){
        val viewDialog = LayoutInflater.from(context).inflate(R.layout.activity_formulario_capitulo, null)

        val editTitulo = viewDialog.edit_titulo_capitulo
        val editDescricao = viewDialog.edit_capitulo_descricao
        val editNTemp = viewDialog.edit_n_temporada
        val editNCapitulo = viewDialog.edit_n_capitulo

        editTitulo.setText(capitulo.titulo)
        editDescricao.setText(capitulo.descricao)
        editNTemp.setText(capitulo.nTemporada.toString())
        editNCapitulo.setText(capitulo.nCapitulo.toString())

        alertDialog.setView(viewDialog)
            .setTitle("Novo Capítulo")
            .setPositiveButton("Salvar"){_, _ ->

                capitulo.titulo = editTitulo.text.toString()
                capitulo.descricao = editDescricao.text.toString()
                capitulo.nCapitulo = editNCapitulo.text.toString().toInt()
                capitulo.nTemporada = editNTemp.text.toString().toInt()

                Snackbar.make(viewDialog,
                    "Adicionado",
                    Snackbar.LENGTH_LONG).show()

                notifyItemChanged(position)
            }
            .setNegativeButton("Cancelar"){_, _ ->}
            .create().show()

    }

    @SuppressLint("InflateParams")
    private fun verNota(alertDialog: AlertDialog.Builder, capitulo: Capitulo, position: Int){

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
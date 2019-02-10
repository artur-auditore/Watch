package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artur.watch.Model.Comentario
import com.example.artur.watch.Model.Post
import com.example.artur.watch.R
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_comentario.view.*
import java.text.SimpleDateFormat
import java.util.*

class ComentarioAdapter(private val context: Context,
                        private val comentarios: MutableList<Comentario>,
                        private val comentarioBox: Box<Comentario>):
    RecyclerView.Adapter<ComentarioAdapter.ViewHolder>() {

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){

        val textNome = itemView.text_comentario_nome
        val textUsername = itemView.text_comentario_username
        val descricao = itemView.text_comentario_descricao
        val data = itemView.text_comentario_data

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(comentario: Comentario){

            textNome.text = comentario.usuario.target.nome
            textUsername.text = "@${comentario.usuario.target.username}"
            descricao.text = comentario.descricao
            data.text = SimpleDateFormat("dd/MM/yyyy - HH:mm").format(comentario.data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false))
    }

    override fun getItemCount(): Int {
        return comentarios.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comentario = comentarios[position]
        holder.bind(comentario)
    }
}
package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.artur.watch.ComentariosActivity
import com.example.artur.watch.Model.*
import com.example.artur.watch.R
import com.example.artur.watch.Util.K
import com.example.artur.watch.Util.K.Companion.DEFAULT_VALUE
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val context: Context,
                  private var listPosts: MutableList<Post>,
                  private val postBox: Box<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    private var usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
    private var usuarioLogado = obterUsuario()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nome = itemView.text_nome
        val username = itemView.text_username
        val texto = itemView.text_post
        val textSerie = itemView.text_post_titulo_serie
        val textEstado = itemView.text_post_estado
        val data = itemView.text_data
        val opcoes = itemView.opcoes_post

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(post: Post){

            nome.text = post.usuario.target.nome
            username.text = "@${post.usuario.target.username}"
            texto.text = post.descricao
            textSerie.text = post.serie.target.titulo
            textEstado.text = post.estadoPost
            data.text = SimpleDateFormat("dd/MM/yyyy - HH:mm").format(post.data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount(): Int {
        return listPosts.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = listPosts[position]
        holder.bind(post)

        menuPop(holder.itemView, post, position)

        holder.textSerie.setOnClickListener {
            val alertDialog  = AlertDialog.Builder(holder.itemView.context)
            alertDialog.setTitle("Informações")
                .setMessage(post.toString())
                .setNeutralButton("OK"){_, _ -> }
                .create().show()
        }

        holder.itemView.setOnClickListener {
            comentar(post, position)
        }

        opcoes(holder.opcoes, post, position)

    }

    private fun opcoes(itemView: View, post: Post, position: Int){

        itemView.setOnClickListener {

            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_pop_post, popup.menu)

            popup.setOnMenuItemClickListener { item ->

                when (item.itemId){

                    R.id.op_excluir_post -> {
                        if (post.usuario.target.id == usuarioLogado.id) excluir(itemView, post, position)
                        else Snackbar.make(itemView, "Você não publicou isto", Snackbar.LENGTH_LONG).show()
                    }
                }
                false
            }

            popup.show()
        }
    }

    private fun comentar(post: Post, position: Int){

        val intent = Intent(context, ComentariosActivity::class.java)
        intent.putExtra(K.ID_POST, post.id)
        context.startActivity(intent)
        notifyItemChanged(position)
    }

    private fun obterUsuario(): Usuario {

        val pref = context.getSharedPreferences(context.getString(R.string.pref_name), MODE_PRIVATE)
        val id = pref.getLong(K.ID_USUARIO, DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    private fun menuPop(itemView: View, post: Post, position: Int){

        itemView.setOnLongClickListener { it ->

            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_pop_post, popup.menu)

            popup.setOnMenuItemClickListener { item ->

                when (item.itemId) {

                    R.id.op_excluir_post -> {
                        if (post.usuario.target.id == usuarioLogado.id) excluir(itemView, post, position)
                    }

                }
                false
            }
            popup.show()
            true
        }
    }

    private fun excluir(view: View, post: Post, position: Int){

        val query = postBox.query()
            .equal(Post_.usuarioId, usuarioLogado.id)
            .build().find()

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Excluir")
            .setMessage("Deseja realmente excluir seu post?")
            .setPositiveButton("SIM"){_, _ ->
                query.remove(post)
                this.listPosts.remove(post)
                this.postBox.remove(post)
                notifyItemChanged(position)
                notifyItemRangeChanged(position, itemCount)
                notifyItemRangeRemoved(position, itemCount)
                Snackbar.make(view, "Post excluído", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("NÃO"){_, _ ->}
            .create()
            .show()
    }
}
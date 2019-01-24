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
import com.example.artur.watch.FormularioPostActivity
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Post_
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.R
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val context: Context,
                  private val postBox: Box<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    companion object {
        const val KEY = "idUsuario"
        const val MODE_PRIVATE = 0x0000
        const val ID = "idPost"
        const val DEFAULT_VALUE: Long = -1
    }

    private var usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
    private var usuarioLogado = obterUsuario()
    private var postsSalvosBox = ObjectBox.boxStore.boxFor(Post::class.java)

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val opcoes = itemView.img_opcoes

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(post: Post){
            val nome = itemView.text_nome
            val username = itemView.text_username
            val texto = itemView.text_post
            val data = itemView.text_data


            val dataAtual = Date()
            nome.text = post.usuario.target.nome
            username.text = "@${post.usuario.target.username}"
            texto.text = post.descricao
            data.text = SimpleDateFormat("dd/MM/yyyy").format(dataAtual)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount(): Int {
        return postBox.all.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postBox.all[position]
        holder.bind(post)

        menuPop(holder.itemView, post, position)

        holder.opcoes.setOnClickListener { it ->
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.menu_opcoes, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.op_salvar_post -> {
                        val postCopy = post
                        postsSalvosBox.put(postCopy)
                        notifyItemChanged(position)
                    }
                }
                false
            }

            popup.show()
        }
    }

    private fun obterUsuario(): Usuario {

        val pref = context.getSharedPreferences("w.file", MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    private fun menuPop(itemView: View, post: Post, position: Int){

        itemView.setOnLongClickListener { it ->
            if (post.usuario.target.id == usuarioLogado.id){

                val popup = PopupMenu(context, it)
                popup.menuInflater.inflate(R.menu.menu_pop, popup.menu)

                popup.setOnMenuItemClickListener { item ->

                    when (item.itemId){

                        R.id.op_editar -> editar(post, position)
                        R.id.op_excluir -> excluir(itemView, post, position)
                    }

                    false
                }

                popup.show()

            } else { }
            true
        }
    }

    private fun editar(post: Post, position: Int){

        val intent = Intent(context, FormularioPostActivity::class.java)
        intent.putExtra(ID, post.id)
        context.startActivity(intent)
        notifyItemChanged(position)
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
                this.postBox.remove(post)
                notifyItemChanged(position)
                notifyItemRangeChanged(position, itemCount)
                Snackbar.make(view, "Post excluído", Snackbar.LENGTH_LONG).show()
            }
            .setNegativeButton("NÃO"){_, _ ->}
            .create()
            .show()
    }
}
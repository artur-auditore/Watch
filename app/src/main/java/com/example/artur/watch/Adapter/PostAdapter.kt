package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artur.watch.Model.Post
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val context: Context,
                  private val posts: MutableList<Post>,
                  private val postBox: Box<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>(){


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

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
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }
}
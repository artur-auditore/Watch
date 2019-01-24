package com.example.artur.watch.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artur.watch.Model.Post
import com.example.artur.watch.R
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_rascunho.view.*

class PostRascunhoAdapter(private val context: Context,
                          private val rascunhos: MutableList<Post>,
                          private val postRascunhoBox: Box<Post>):
    RecyclerView.Adapter<PostRascunhoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rascunho, parent, false))
    }

    override fun getItemCount(): Int {
        return rascunhos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = rascunhos[position]
        holder.bind(post)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(post: Post){
            val text = itemView.text_post_rascunho

            text.text = post.descricao
        }
    }
}
package com.example.artur.watch.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import com.example.artur.watch.Model.Comentario
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.R
import com.example.artur.watch.Util.K
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.item_comentario.view.*
import java.text.SimpleDateFormat
import java.util.*

class ComentarioAdapter(private val context: Context,
                        private val comentarios: MutableList<Comentario>,
                        private val comentarioBox: Box<Comentario>):
    RecyclerView.Adapter<ComentarioAdapter.ViewHolder>() {

    private var usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
    private var usuarioLogado = obterUsuario()

    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){


        val textNome = itemView.text_comentario_nome

        val textUsername = itemView.text_comentario_username
        val descricao = itemView.text_comentario_descricao
        val data = itemView.text_comentario_data
        val usernameResp = itemview.text_username_comentario
        val opcoes = itemview.opcoes_comentario

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(comentario: Comentario){

            usernameResp.text = "@${comentario.post.target.usuario.target.username}"
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

        menuPop(holder.itemView, comentario, position)

        excluir(holder.opcoes, comentario, position)
    }

    private fun obterUsuario(): Usuario {

        val pref = context.getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE)
        val id = pref.getLong(K.ID_USUARIO, K.DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    private fun obterDono(comentario: Comentario): Boolean{
        return when {
            comentario.usuario.target.id == usuarioLogado.id -> true
            comentario.post.target.usuario.target.id == usuarioLogado.id -> true
            else -> false
        }
    }

    private fun excluir(itemview: View, comentario: Comentario, position: Int){

        itemview.setOnClickListener {

            if (obterDono(comentario)){
                val popup = PopupMenu(context, it)
                popup.menuInflater.inflate(R.menu.menu_pop_post, popup.menu)

                popup.setOnMenuItemClickListener { item ->

                    when(item.itemId){
                        R.id.op_excluir_post ->{

                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Excluir")
                                .setMessage("Deseja realmente excluir seu comentário?")
                                .setPositiveButton("Sim"){_, _ ->

                                    this.comentarios.remove(comentario)
                                    this.comentarioBox.remove(comentario)
                                    notifyItemChanged(position)
                                    notifyItemRangeChanged(position, itemCount)

                                    Toast.makeText(
                                        context,
                                        "Apagado.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .setNegativeButton("Não"){_, _ ->}.create().show()
                        }
                    }
                    false
                }
                popup.show()

            } else {
                Snackbar.make(it, context.getString(R.string.erro_post), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun menuPop(itemview: View, comentario: Comentario, position: Int){

        itemview.setOnLongClickListener { it ->

            if (obterDono(comentario)){
                val popup = PopupMenu(context, it)
                popup.menuInflater.inflate(R.menu.menu_pop_post, popup.menu)

                popup.setOnMenuItemClickListener { item ->

                    when(item.itemId){
                        R.id.op_excluir_post ->{

                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Excluir")
                                .setMessage("Deseja realmente excluir seu comentário?")
                                .setPositiveButton("Sim"){_, _ ->

                                    this.comentarios.remove(comentario)
                                    this.comentarioBox.remove(comentario)
                                    notifyItemChanged(position)
                                    notifyItemRangeChanged(position, itemCount)

                                    Toast.makeText(
                                        context,
                                        "Comentário Apagado.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .setNegativeButton("Não"){_, _ ->}.create().show()
                        }
                    }
                    false
                }
                popup.show()

            } else {
                Snackbar.make(it, context.getString(R.string.erro_post), Snackbar.LENGTH_LONG).show()

            }
            false
        }
    }
}

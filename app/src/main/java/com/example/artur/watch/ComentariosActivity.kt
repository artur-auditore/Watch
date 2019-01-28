package com.example.artur.watch

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.example.artur.watch.Adapter.ComentarioAdapter
import com.example.artur.watch.Adapter.PostAdapter
import com.example.artur.watch.Model.Comentario
import com.example.artur.watch.Model.Comentario_
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_comentarios.*
import kotlinx.android.synthetic.main.comentario_dialog.view.*

class ComentariosActivity : AppCompatActivity() {

    private lateinit var textNome: TextView
    private lateinit var textUsername: TextView
    private lateinit var textSerie: TextView
    private lateinit var textEstado: TextView
    private lateinit var textDescricao: TextView
    private lateinit var textComentar: TextView

    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuarioLogado: Usuario
    private lateinit var comentarioBox: Box<Comentario>
    private lateinit var comentario: Comentario
    private lateinit var postBox: Box<Post>
    private lateinit var postAtual: Post

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)

        bind()

        comentar()

    }

    private fun obterUsuario(): Usuario {

        val pref = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val id = pref.getLong(TimeLineActivity.KEY, TimeLineActivity.DEFAULT_VALUE)
        val usuario = usuarioBox.get(id)
        return usuario
    }

    override fun onResume() {
        super.onResume()

        val list = comentarioBox.query()
            .equal(Comentario_.postId, postAtual.id)
            .build().find()

        loadComentarios(list)
    }

    private fun loadComentarios(list: MutableList<Comentario>){

        recyclerView.adapter = ComentarioAdapter(this, list, comentarioBox)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("SetTextI18n")
    private fun bind(){

        textNome = text_nome_post
        textUsername = text_username_post
        textDescricao = text_post_descricao
        textSerie = text_post_titulo_serie
        textEstado = text_post_estado
        textComentar = text_comentar
        recyclerView = rv_comentarios

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        usuarioLogado = obterUsuario()

        val postId = intent.getLongExtra(PostAdapter.ID, PostAdapter.DEFAULT_VALUE)
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)

        comentarioBox = ObjectBox.boxStore.boxFor(Comentario::class.java)
        comentario = Comentario()

        postAtual = postBox.get(postId)

        textNome.text = postAtual.usuario.target.nome
        textUsername.text = "@${postAtual.usuario.target.username}"
        textSerie.text = postAtual.serie.target.titulo
        textEstado.text = postAtual.estadoPost
        textDescricao.text = postAtual.descricao
    }

    @SuppressLint("InflateParams")
    private fun comentar(){
        textComentar.setOnClickListener {

            val alertdialog = AlertDialog.Builder(this)
            val viewDialog = layoutInflater.inflate(R.layout.comentario_dialog, null)

            val editdecricao = viewDialog.edit_comentario_descricao

            alertdialog.setView(viewDialog)
                .setTitle("Comentar")
                .setPositiveButton("Publicar"){_, _ ->

                    comentario.descricao = editdecricao.text.toString()
                    comentario.post.target = postAtual
                    comentario.usuario.target = usuarioLogado
                    comentarioBox.put(comentario)

                    val list = comentarioBox.query()
                        .equal(Comentario_.postId, postAtual.id)
                        .build().find()

                    loadComentarios(list)
                }
                .setNegativeButton("Cancelar"){_, _ ->}
                .create().show()

        }

    }
}

package com.example.artur.watch

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.artur.watch.Adapter.ComentarioAdapter
import com.example.artur.watch.Model.Comentario
import com.example.artur.watch.Model.Comentario_
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.Util.K
import com.example.artur.watch.Util.K.Companion.DEFAULT_VALUE
import com.example.artur.watch.Util.K.Companion.ID_POST
import com.example.artur.watch.Util.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_comentarios.*
import java.text.SimpleDateFormat
import java.util.*

class ComentariosActivity : AppCompatActivity() {

    private lateinit var textNome: TextView
    private lateinit var textUsername: TextView
    private lateinit var textSerie: TextView
    private lateinit var textEstado: TextView
    private lateinit var textDescricao: TextView
    private lateinit var textData: TextView
    private lateinit var textComentar: TextView

    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuarioLogado: Usuario
    private lateinit var comentarioBox: Box<Comentario>
    private lateinit var postBox: Box<Post>
    private lateinit var postAtual: Post

    private lateinit var recyclerView: RecyclerView
    private lateinit var editComentario: EditText
    private lateinit var buttonPublicar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)

        bind()
        escreverComentario()

    }

    private fun obterUsuario(): Usuario {

        val pref = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val id = pref.getLong(K.ID_USUARIO, DEFAULT_VALUE)
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

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun bind(){

        textNome = text_nome_post
        textUsername = text_username_post
        textDescricao = text_post_descricao
        textSerie = text_post_titulo_serie
        textEstado = text_post_estado
        textData = text_data_post
        textComentar = text_comentar
        recyclerView = rv_comentarios
        buttonPublicar = button_comentar
        editComentario = edit_comentario_descricao

        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        usuarioLogado = obterUsuario()

        val postId = intent.getLongExtra(ID_POST, DEFAULT_VALUE)
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)

        comentarioBox = ObjectBox.boxStore.boxFor(Comentario::class.java)

        postAtual = postBox.get(postId)

        textNome.text = postAtual.usuario.target.nome
        textUsername.text = "@${postAtual.usuario.target.username}"
        textSerie.text = postAtual.serie.target.titulo
        textEstado.text = postAtual.estadoPost
        textDescricao.text = postAtual.descricao
        textData.text = SimpleDateFormat("dd/MM/yyyy - HH:mm").format(postAtual.data)
    }

    @SuppressLint("InflateParams")
    private fun escreverComentario(){
        textComentar.setOnClickListener {

            editComentario.visibility = View.VISIBLE
            buttonPublicar.visibility = View.VISIBLE
        }

        buttonPublicar.setOnClickListener {
            publicar()
        }
    }

    private fun publicar(){

        val descricao = editComentario.text.toString()

        if (descricao.trim() == ""){

            Toast.makeText(
                this,
                "Escreva algo antes de prosseguir",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val dataAtual = Date()
            val comentario = Comentario()
            comentario.descricao = descricao
            comentario.post.target = postAtual
            comentario.usuario.target = usuarioLogado
            comentario.data = dataAtual
            comentarioBox.put(comentario)

            editComentario.text.clear()
            editComentario.visibility = View.GONE
            buttonPublicar.visibility = View.GONE

            Toast.makeText(
                this,
                "Comentário Publicado",
                Toast.LENGTH_LONG
            ).show()

            val list = comentarioBox.query()
                .equal(Comentario_.postId, postAtual.id).build().find()

            loadComentarios(list)

        }
    }
}

package com.example.artur.watch

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_formulario_post.*
import java.util.*

class FormularioPostActivity : AppCompatActivity() {

    companion object {
        const val ID = "idPost"
        const val KEY = "idUsuario"
        const val DEFAULT_VALUE: Long = -1
    }

    private lateinit var editPostDescricao: EditText
    private lateinit var postBox: Box<Post>
    private lateinit var usuarioBox: Box<Usuario>

    private lateinit var usuarioLogado: Usuario
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_post)

        bind()

        //Para editar
        val id = intent.getLongExtra(ID, DEFAULT_VALUE)
        if (id != DEFAULT_VALUE){
            post = postBox.get(id)
            editPostDescricao.setText(post.descricao)

        }

    }

    private fun bind(){

        editPostDescricao = post_descricao
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)
        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        usuarioLogado = obterUsuario()

        post = Post()
    }

    private fun obterUsuario(): Usuario {

        val pref = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_compartilhar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){ R.id.op_publicar -> publicar()}
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    private fun publicar(){

        //TODO autoCompleteText depois...
        val textPost = editPostDescricao.text.toString()

        post.descricao = textPost
        post.data = Date()
        post.usuario.target = usuarioLogado
        postBox.put(post)
        finish()
    }
}

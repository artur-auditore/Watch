package com.example.artur.watch

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.artur.watch.Model.*
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
    private lateinit var serieBox: Box<Serie>
    private lateinit var postRascunhoBox: Box<Post>
    private lateinit var usuarioBox: Box<Usuario>
    private lateinit var usuarioLogado: Usuario
    private lateinit var acItem: AutoCompleteTextView

    private lateinit var post: Post
    private lateinit var serie: Serie

    private lateinit var radioEstaAssistindo: RadioButton
    private lateinit var radioIraAssistir: RadioButton
    private lateinit var radioJaAssitiu: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_post)

        bind()


        //Para editar
        val id = intent.getLongExtra(ID, DEFAULT_VALUE)
        if (id != DEFAULT_VALUE){
            supportActionBar!!.title = getString(R.string.editar_post)
            post = postBox.get(id)
            editPostDescricao.setText(post.descricao)
            acItem.setText(post.serie.target.titulo)

        }
    }

    override fun onResume() {
        super.onResume()

        val list = serieBox.query()
            .equal(Serie_.usuarioId, usuarioLogado.id)
            .build()
            .find()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        acItem.setAdapter(adapter)

        acItem.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            serie = parent.getItemAtPosition(position) as Serie
        }

    }

    private fun bind(){

        editPostDescricao = post_descricao
        acItem = auto_complete_filme_serie
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)
        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        usuarioLogado = obterUsuario()

        post = Post()

        radioEstaAssistindo = esta_assistindo
        radioIraAssistir = ira_assistir
        radioJaAssitiu = ja_assistiu

        postRascunhoBox = ObjectBox.boxStore.boxFor(Post::class.java)

    }

    private fun obterUsuario(): Usuario {

        val pref = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        return usuarioBox.get(id)
    }

    override fun onBackPressed() {

        if (editPostDescricao.text.toString().trim() != ""){

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Arquivar")
                .setMessage("Deseja arquivar o post?")
                .setPositiveButton("SIM"){_ , _ ->
                    salvarRascunho()
                }
                .setNegativeButton("NÃO"){_, _ ->
                    super.onBackPressed()
                }
                .create()
                .show()
        } 
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_compartilhar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.op_publicar -> publicar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun publicar(){

        val textPost = editPostDescricao.text.toString()
        val textItem = acItem.text.toString()

        try {

            if (textItem.trim() == ""){

                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Erro")
                    .setMessage("Há campos obrigatórios em branco ou você não adiciou nenhuma série ainda. " +
                            "Adicione uma série e/ou sua opinião para prosseguir.")
                    .setNegativeButton("OK"){_, _ ->}
                    .create().show()

            } else {

                if (!radioEstaAssistindo.isChecked || !radioIraAssistir.isChecked || !radioJaAssitiu.isChecked){

                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Erro")
                        .setMessage("Campos obrigatórios ainda estão em branco")
                        .setNegativeButton("OK"){_, _ ->}
                        .create().show()

                } else {

                    when{
                        radioEstaAssistindo.isChecked -> post.estadoPost = "${radioEstaAssistindo.text }"
                        radioIraAssistir.isChecked -> post.estadoPost = "${radioIraAssistir.text }"
                        radioJaAssitiu.isChecked -> post.estadoPost = "${radioJaAssitiu.text }"
                    }

                    post.descricao = textPost
                    post.serie.target = serie
                    post.data = Date()
                    post.usuario.target = usuarioLogado
                    postBox.put(post)
                    finish()
                }

            }

        } catch (e: UninitializedPropertyAccessException){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Erro")
                .setMessage("Você não adicionou nenhuma série com este título. " +
                        "Adicione-a para prosseguir.")
                .setNeutralButton("OK"){_, _ ->}
                .create().show()
        }


    }

    private fun salvarRascunho(){

        val textPost = editPostDescricao.text.toString()

        if (textPost.trim() == ""){

            Toast.makeText(this, "Escreva alguma coisa antes de prosseguir",
                Toast.LENGTH_LONG).show()
        } else {

            post.descricao = textPost
            post.data = Date()
            post.isArquivado = true
            post.usuario.target = usuarioLogado
            postRascunhoBox.put(post)
            finish()
        }
    }
}

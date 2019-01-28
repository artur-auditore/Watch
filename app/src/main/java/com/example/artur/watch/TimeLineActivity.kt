package com.example.artur.watch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.artur.watch.Adapter.*
import com.example.artur.watch.Fragments.SeriesFragment
import com.example.artur.watch.Model.*
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_time_line.*
import kotlinx.android.synthetic.main.app_bar_time_line.*
import kotlinx.android.synthetic.main.content_time_line.*
import kotlinx.android.synthetic.main.nav_header_time_line.view.*

class TimeLineActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val KEY = "idUsuario"
        const val DEFAULT_VALUE: Long = -1
        const val REQUEST_CODE = 1
    }

    private lateinit var preferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var postBox: Box<Post>
    private lateinit var postRascunhoBox: Box<Post>
    private lateinit var postsSalvos: Box<Post>
    private lateinit var fabNovoPost: FloatingActionButton

    private lateinit var textNome: TextView
    private lateinit var textEmail: TextView
    private lateinit var navigationView: NavigationView

    private lateinit var usuarioLogado: Usuario
    private lateinit var usuarioBox: Box<Usuario>

    private lateinit var serieBox: Box<Serie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (!logado()){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        bind()


        fabNovoPost.setOnClickListener {
            startActivity(Intent(this, FormularioPostActivity::class.java))
        }

        loadPosts()
    }

    private fun bind(){

        recyclerView = rv_posts
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)
        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        fabNovoPost = fab_novo_post
        usuarioLogado = obterUsuario()

        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
        postRascunhoBox = ObjectBox.boxStore.boxFor(Post::class.java)
        postsSalvos = ObjectBox.boxStore.boxFor(Post::class.java)

        navigationView = nav_view
        val nav = navigationView.getHeaderView(0)
        textNome = nav.text_nome_usuario
        textEmail = nav.text_email_do_usuario

        textNome.text = usuarioLogado.nome
        textEmail.text = usuarioLogado.email
    }

    //Métodos específicos para login e logout
    private fun logado(): Boolean {

        preferences = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val usuarioID = preferences.getLong(KEY, DEFAULT_VALUE)
        return usuarioID != DEFAULT_VALUE
    }


    private fun obterUsuario(): Usuario {

        val pref = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        val usuario = usuarioBox.get(id)
        return usuario
    }

    @SuppressLint("CommitPrefEdits")
    private fun logout() {

        preferences = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE)
        preferences.edit().clear()
        preferences.edit().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    //Métodos para carregar itens para recyclerView
    private fun loadPosts(){

        recyclerView.adapter = PostAdapter(this, postBox)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun loadSeries(list: MutableList<Serie>){

        val adapter = SeriesAdapter(this, list, serieBox)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
    }

    private fun loadRascunhos(list: MutableList<Post>){

        recyclerView.adapter = PostRascunhoAdapter(this, list, postRascunhoBox)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
    }

    //Métodos de menus e navigation drawer
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.time_line, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) { R.id.action_logout -> logout() }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        loadPosts()
    }

    @SuppressLint("RestrictedApi")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.posts -> {
                fabNovoPost.visibility = View.VISIBLE

                supportActionBar!!.title = getString(R.string.feed_principal)
                loadPosts()
            }

            R.id.series -> {
                fabNovoPost.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction().replace(R.id.main, SeriesFragment()).commit()


                val list = serieBox.query()
                    .equal(Serie_.usuarioId, usuarioLogado.id)
                    .build().find()

                supportActionBar!!.title = getString(R.string.s_ries)
                loadSeries(list)
            }

            R.id.posts_rascunhos -> {

                supportActionBar!!.title = getString(R.string.rascunho)

                val list = postRascunhoBox.query()
                    .equal(Post_.isArquivado, usuarioLogado.id)
                    .build().find()

                for (post in list) if (post.isArquivado) loadRascunhos(list)


            }

            R.id.perfil -> {

                supportActionBar!!.title = getString(R.string.perfil)
            }

            R.id.salvos -> {
                supportActionBar!!.title = getString(R.string.salvos)

                val list = postsSalvos.query()
                    .equal(Post_.isArquivado, usuarioLogado.id)
                    .build().find()

                recyclerView.adapter = PostsSalvosAdapter(this, list, postsSalvos)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.hasFixedSize()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

package com.example.artur.watch

import android.annotation.SuppressLint
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
import com.example.artur.watch.Adapter.FilmeAdapter
import com.example.artur.watch.Adapter.PostAdapter
import com.example.artur.watch.Adapter.SeriesAdapter
import com.example.artur.watch.Fragments.FilmesFragment
import com.example.artur.watch.Model.*
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_time_line.*
import kotlinx.android.synthetic.main.app_bar_time_line.*
import kotlinx.android.synthetic.main.content_time_line.*

class TimeLineActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val KEY = "idUsuario"
        const val DEFAULT_VALUE: Long = -1
    }

    private lateinit var preferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var postBox: Box<Post>
    private lateinit var fabNovoPost: FloatingActionButton

    private lateinit var usuarioLogado: Usuario
    private lateinit var usuarioBox: Box<Usuario>

    private lateinit var filmeBox: Box<Filme>
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
    }

    private fun bind(){
        recyclerView = rv_posts
        postBox = ObjectBox.boxStore.boxFor(Post::class.java)
        usuarioBox = ObjectBox.boxStore.boxFor(Usuario::class.java)
        fabNovoPost = fab_novo_post
        usuarioLogado = obterUsuario()

        filmeBox = ObjectBox.boxStore.boxFor(Filme::class.java)
        serieBox = ObjectBox.boxStore.boxFor(Serie::class.java)
    }

    private fun logado(): Boolean {
        preferences = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val usuarioID = preferences.getLong(KEY, DEFAULT_VALUE)
        return usuarioID != DEFAULT_VALUE
    }

    private fun obterUsuario(): Usuario {
        val pref = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE)
        val usuario = usuarioBox.get(id)
        return usuario
    }


    @SuppressLint("CommitPrefEdits")
    private fun logout() {
        preferences = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        preferences.edit().clear()
        preferences.edit().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter = PostAdapter(this, postBox.all, postBox)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
    }

    private fun loadFilmes(list: MutableList<Filme>){

        recyclerView.adapter = FilmeAdapter(this, list, filmeBox)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
    }

    private fun loadSeries(list: MutableList<Serie>){

        recyclerView.adapter = SeriesAdapter(this, list, serieBox)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
    }

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

    @SuppressLint("CommitTransaction", "RestrictedApi")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.posts -> {
                fabNovoPost.visibility = View.VISIBLE

                recyclerView.adapter = PostAdapter(this, postBox.all, postBox)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.hasFixedSize()
            }

            R.id.filmes -> {
                fabNovoPost.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction().replace(R.id.main, FilmesFragment()).commit()

                val query = filmeBox.query()
                val list = query.equal(Filme_.usuarioId, usuarioLogado.id)
                    .build().find()

                loadFilmes(list)
            }

            R.id.series -> {
                fabNovoPost.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction().replace(R.id.main, FilmesFragment()).commit()

                val query = serieBox.query()
                val list = query.equal(Serie_.usuarioId, usuarioLogado.id)
                    .build().find()

                loadSeries(list)
            }

            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

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
import com.example.artur.watch.Adapter.PostAdapter
import com.example.artur.watch.Fragments.FilmesFragment
import com.example.artur.watch.Model.Post
import com.example.artur.watch.Model.Usuario
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_time_line.*
import kotlinx.android.synthetic.main.app_bar_time_line.*
import kotlinx.android.synthetic.main.content_time_line.*

class TimeLineActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val KEY = "idUsuario"
        const val DEFAULT_VALUE = -1
    }

    private lateinit var preferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var postBox: Box<Post>
    private lateinit var fabNovoPost: FloatingActionButton

    private lateinit var usuarioLogado: Usuario
    private lateinit var usuarioBox: Box<Usuario>

    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
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
    }

    private fun logado(): Boolean {
        preferences = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val usuarioID = preferences.getLong(KEY, DEFAULT_VALUE.toLong())
        return usuarioID != DEFAULT_VALUE.toLong()
    }

    private fun obterUsuario(): Usuario {
        val pref = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val id = pref.getLong(KEY, DEFAULT_VALUE.toLong())
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

        adapter = PostAdapter(this, postBox.all, postBox)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.time_line, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) { R.id.action_logout -> logout() }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CommitTransaction", "RestrictedApi")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.posts -> {
                fabNovoPost.visibility = View.VISIBLE
            }
            R.id.filmes -> {
                fabNovoPost.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction().replace(R.id.main, FilmesFragment()).commit()
            }
            R.id.series -> {

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

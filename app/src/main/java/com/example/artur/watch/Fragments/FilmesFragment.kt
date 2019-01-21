package com.example.artur.watch.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import com.example.artur.watch.FormularioItemActivity
import com.example.artur.watch.R
import kotlinx.android.synthetic.main.filmes_layout.view.*

class FilmesFragment: Fragment() {

    private lateinit var myView: View
    private lateinit var fabNovoFilme: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.filmes_layout, container, false)

        fabNovoFilme = myView.fab_novo_filme
        fabNovoFilme.setOnClickListener {
            startActivity(Intent(myView.context, FormularioItemActivity::class.java))
        }

        return myView
    }
}
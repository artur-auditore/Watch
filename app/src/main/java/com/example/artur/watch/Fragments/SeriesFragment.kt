package com.example.artur.watch.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.artur.watch.Adapter.SeriesAdapter
import com.example.artur.watch.Adapter.TabsAdapter
import com.example.artur.watch.FormularioSerieActivity
import com.example.artur.watch.Model.Serie
import com.example.artur.watch.Model.Serie_
import com.example.artur.watch.R
import com.example.artur.watch.dal.ObjectBox
import io.objectbox.Box
import kotlinx.android.synthetic.main.series_layout.*
import kotlinx.android.synthetic.main.series_layout.view.*

class SeriesFragment: Fragment() {

    private lateinit var myView: View
    private lateinit var fabNovaSerie: FloatingActionButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.series_layout, container, false)

        fabNovaSerie = myView.fab_nova_serie
        fabNovaSerie.setOnClickListener {
            startActivity(Intent(myView.context, FormularioSerieActivity::class.java))
        }
        setHasOptionsMenu(true)

        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_opcoes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.op_excluir_lista ->{

            }
            R.id.op_nova_lista ->{

            }
        }

        return super.onOptionsItemSelected(item)
    }

}
package com.example.artur.watch.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TabsAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment) {

    private val listFragment = mutableListOf<Fragment>()
    private val list = mutableListOf<String>()

    fun add(frag: Fragment, title: String) {
        this.listFragment.add(frag)
        this.list.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position]
    }
}
package com.example.artur.watch.Util

import android.app.Application
import android.content.Context
import com.example.artur.watch.Model.MyObjectBox
import io.objectbox.BoxStore

class App: Application(){
    companion object Constants {
        const val TAG = "ObjectBoxExample"
    }

    override fun onCreate() {
        super.onCreate()
        ObjectBox.build(this)
    }
}

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun build(context: Context) {
        // This is the minimal setup required on Android
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext).build()

        // Example how you could use a custom dir in "external storage"
        // (Android 6+ note: give the app storage permission in app info settings)
//        val directory = File(Environment.getExternalStorageDirectory(), "objectbox-notes");
//        boxStore = MyObjectBox.builder().androidContext(context.applicationContext)
//                .directory(directory)
//                .build()
    }
}
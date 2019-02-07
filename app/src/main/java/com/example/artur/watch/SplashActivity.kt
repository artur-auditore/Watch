package com.example.artur.watch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.example.artur.watch.Util.K
import com.example.artur.watch.Util.K.Companion.DEFAULT_VALUE

class SplashActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()

        if (!logado()){
            handler.postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 800)
        } else {
            handler.postDelayed({
                startActivity(Intent(this, TimeLineActivity::class.java))
                finish()
            }, 800)
        }
    }

    private fun logado(): Boolean {
        preferences = getSharedPreferences("w.file", Context.MODE_PRIVATE)
        val usuarioID = preferences.getLong(K.ID_USUARIO, DEFAULT_VALUE)
        return usuarioID != K.DEFAULT_VALUE
    }
}

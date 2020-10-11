package com.parcial1.parcial1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun VerMaterias(view: View) {
        var intent = Intent(this@MainActivity,ListaMateriaActivity::class.java)
        startActivity(intent)
    }
}
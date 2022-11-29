package com.example.mungnyang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.example.mungnyang.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val menuItem = menu?.findItem(R.id.menu_chat)

        menuItem?.setOnMenuItemClickListener {
            Toast.makeText(applicationContext,"Direction Message로 이동합니다.", Toast.LENGTH_SHORT).show()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }
}
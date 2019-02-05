package ch.example.fitbitpoints.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import ch.example.fitbitpoints.R

class MainActivity : AppCompatActivity() {

    override fun onNewIntent(intent: Intent?) { // used to manage result from TabIntent
        super.onNewIntent(intent)
        Log.i("MainActivity", "onNewIntent called")
        setIntent(intent)
        reloadMainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        reloadMainFragment()
    }

    private fun reloadMainFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.content, MainFragment()).commit()
    }
}

package com.example.movierproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.start_menu.*

class StartMenuActivity : AppCompatActivity() {
    companion object {
        var TAG = StartMenuActivity::class.java.name
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_menu)
        setupButtons()
        setupGenreSelector()
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_help ->{
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.option_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    fun setupButtons() {
        start_session_btn.setOnClickListener {
            run {
                switchToMovieSelectingActivity()
            }
        }

        join_session_btn.setOnClickListener {
            run {
                handleJoinClick()
            }
        }
    }

    fun handleJoinClick() {
        val key = session_key_input.text.toString()
        val regex = "#[0-9]{5}".toRegex()
        if (!regex.matches(key)) {
            val animShake = AnimationUtils.loadAnimation(this, R.anim.shake)
            session_key_input.startAnimation(animShake)
        } else {
            switchToMovieSelectingActivity()
        }
    }

    fun setupGenreSelector() {
        Ion.with(this)
            .load("GET", "https://api.themoviedb.org/3/genre/movie/list?")
            .addQuery("api_key", resources.getString(R.string.api_key))
            .addQuery("language", "en-US")
            .asJsonObject()
            .setCallback { e, result ->
                val genres = result["genres"].asJsonArray
                val genreArray = mutableListOf<String>()
                genres.forEach { genre ->
                    val name = genre.asJsonObject["name"].toString()
                    genreArray.add(name.substring(1, name.length-1))
                }
                val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, genreArray)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                genre_select.adapter = aa
            }
    }

    fun switchToMovieSelectingActivity(){
        val intent = Intent(this, MovieSelectingActivity::class.java)
        startActivity(intent)
    }

}
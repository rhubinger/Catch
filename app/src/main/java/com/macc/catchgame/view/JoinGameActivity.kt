package com.macc.catchgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.macc.catchgame.R

class JoinGameActivity : AppCompatActivity() {

    private lateinit var buttonJoinGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)

        buttonJoinGame = findViewById(R.id.buttonJoinGame)

        buttonJoinGame.setOnClickListener {
            var intent = Intent(applicationContext, AwaitStartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}
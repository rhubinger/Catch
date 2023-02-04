package com.macc.catchgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.macc.catchgame.R

class MenuActivity : AppCompatActivity() {

    private lateinit var buttonCreateGame: Button
    private lateinit var buttonJoinGame: Button
    private lateinit var textViewLogOut: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        buttonCreateGame = findViewById(R.id.buttonCreateGame)
        buttonJoinGame = findViewById(R.id.buttonJoinGame)
        textViewLogOut = findViewById(R.id.textViewLogOut)

        buttonCreateGame.setOnClickListener {
            var intent = Intent(applicationContext, CreateGameActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        buttonJoinGame.setOnClickListener {
            var intent = Intent(applicationContext, JoinGameActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        textViewLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            var intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}
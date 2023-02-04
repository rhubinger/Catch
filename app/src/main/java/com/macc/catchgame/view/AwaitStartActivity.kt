package com.macc.catchgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.macc.catchgame.R

class AwaitStartActivity : AppCompatActivity() {

    private lateinit var buttonLeaveGame : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_await_start)

        buttonLeaveGame = findViewById(R.id.buttonLeaveGame)

        buttonLeaveGame.setOnClickListener {
            var intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}
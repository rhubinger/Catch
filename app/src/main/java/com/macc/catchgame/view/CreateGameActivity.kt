package com.macc.catchgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.macc.catchgame.R
import kotlin.random.Random

class CreateGameActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var buttonStartGame: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        timePicker = findViewById(R.id.timePicker)
        buttonStartGame = findViewById(R.id.buttonStartGame)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        timePicker.setIs24HourView(true)
        timePicker.hour = 1
        timePicker.minute = 0

        buttonStartGame.setOnClickListener {
            var gameLength : Int = timePicker.minute + 60 * timePicker.hour
            val game = hashMapOf(
                "length" to gameLength,
                "state" to "created",
                "players" to arrayListOf(auth.currentUser?.email.toString()),
            )


            var gameId = Random.nextInt(100000,999999).toString()

            db.collection("games").document(gameId).set(game).addOnSuccessListener {
                var intent = Intent(applicationContext, StartGameActivity::class.java)
                intent.putExtra("gameId", gameId)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to create game", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
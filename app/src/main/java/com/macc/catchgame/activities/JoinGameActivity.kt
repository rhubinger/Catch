package com.macc.catchgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.macc.catchgame.R

class JoinGameActivity : AppCompatActivity() {

    private lateinit var editTextGameId: EditText
    private lateinit var buttonJoinGame: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)

        editTextGameId = findViewById(R.id.editTextGameId)
        buttonJoinGame = findViewById(R.id.buttonJoinGame)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        buttonJoinGame.setOnClickListener {
            var gameId = editTextGameId.text.toString()
            if(TextUtils.isEmpty(gameId)){
                Toast.makeText(this, "GameId field can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val game = db.collection("games").document(gameId)
            game.update("players", FieldValue.arrayUnion(auth.currentUser?.email.toString()))
                .addOnSuccessListener {
                    var intent = Intent(applicationContext, AwaitStartActivity::class.java)
                    intent.putExtra("gameId", gameId)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to join game", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
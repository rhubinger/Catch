package com.macc.catchgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.macc.catchgame.R

class AwaitStartActivity : AppCompatActivity() {

    private lateinit var gameId: String

    private lateinit var textViewGameId: TextView
    private lateinit var buttonLeaveGame : Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_await_start)

        textViewGameId = findViewById(R.id.textViewGameId)
        buttonLeaveGame = findViewById(R.id.buttonLeaveGame)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val extras = intent.extras
        if (extras != null) {
            gameId = extras.getString("gameId").toString()
            textViewGameId.text = gameId.slice(0..2) + " " + gameId.slice (3 .. 5)
        } else {
            Toast.makeText(this, "Failed to get the gameId", Toast.LENGTH_SHORT).show()
        }

        buttonLeaveGame.setOnClickListener {
            val game = db.collection("games").document(gameId)
            game.update("players", FieldValue.arrayRemove(auth.currentUser?.email.toString()))
                .addOnSuccessListener {
                    var intent = Intent(applicationContext, MenuActivity::class.java)
                    intent.putExtra("gameId", gameId)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to leave game", Toast.LENGTH_SHORT).show()
                }
        }

        db.collection("games").document(gameId).
        addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val state = snapshot.data?.get("state").toString()
                if(state.equals("started")){
                    var intent = Intent(applicationContext, GameActivity::class.java)
                    intent.putExtra("gameId", gameId)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }

                val users = snapshot.data?.get("players") as ArrayList<String>
                if(!users.contains(auth.currentUser?.email.toString())){
                    Toast.makeText(this, "You got kicked out of this game", Toast.LENGTH_SHORT).show()

                    var intent = Intent(applicationContext, MenuActivity::class.java)
                    intent.putExtra("gameId", gameId)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
        }
    }
}
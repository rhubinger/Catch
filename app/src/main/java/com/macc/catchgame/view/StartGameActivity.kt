package com.macc.catchgame.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.macc.catchgame.R
import com.macc.catchgame.control.StartGameUserAdapter

class StartGameActivity : AppCompatActivity() {

    private lateinit var gameId : String

    private val playerList = ArrayList<String>()
    private lateinit var userAdapter: StartGameUserAdapter

    private lateinit var textViewGameId: TextView
    private lateinit var buttonStartGame : Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        textViewGameId = findViewById(R.id.textViewGameId)
        buttonStartGame = findViewById(R.id.buttonStartGame)
        recyclerView = findViewById(R.id.recyclerViewStartGame)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val extras = intent.extras
        if (extras != null) {
            gameId = extras.getString("gameId").toString()
            textViewGameId.text = gameId.slice(0..2) + " " + gameId.slice (3 .. 5)
        } else {
            Toast.makeText(this, "Failed to get the gameId", Toast.LENGTH_SHORT).show()
        }

        buttonStartGame.setOnClickListener {
            val game = hashMapOf(
                "state" to "started",
            )
            db.collection("games").document(gameId).set(game, SetOptions.merge())
                .addOnSuccessListener {
                    var intent = Intent(applicationContext, GameActivity::class.java)
                    intent.putExtra("gameId", gameId)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to start game", Toast.LENGTH_SHORT).show()
                }
        }

        db.collection("games").document(gameId).
        addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val players = snapshot.data?.get("players") as ArrayList<String>
                playerList.clear()
                for(player in players){
                    playerList.add(player)
                }
                userAdapter.notifyDataSetChanged()
            }
        }
        userAdapter = StartGameUserAdapter(playerList, gameId)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter
    }
}

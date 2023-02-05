package com.macc.catchgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.macc.catchgame.R
import com.macc.catchgame.adapters.CatchAdapter

class ResultActivity : AppCompatActivity() {

    private lateinit var gameId: String

    private lateinit var textViewResult: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonFinish: Button

    private val catchList = ArrayList<HashMap<String, String>>()
    private lateinit var catchAdapter: CatchAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        textViewResult = findViewById(R.id.textViewResult)
        buttonFinish = findViewById(R.id.buttonFinish)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val extras = intent.extras
        if (extras != null) {
            gameId = extras.getString("gameId").toString()
        } else {
            Toast.makeText(this, "Failed to get the gameId", Toast.LENGTH_SHORT).show()
        }

        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val catchers = document.data?.get("catchers") as ArrayList<String>
                    val players = document.data?.get("players") as ArrayList<String>
                    if(catchers.size == players.size){
                        textViewResult.text = getString(R.string.win_msg)
                    } else {
                        textViewResult.text = getString(R.string.lose_msg)
                    }
                } else {
                    Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get result from db", Toast.LENGTH_SHORT).show()
            }

        buttonFinish.setOnClickListener {
            db.collection("games").document(gameId).delete()
            var intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewResult)
        catchAdapter = CatchAdapter(catchList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = catchAdapter
        db.collection("games").document(gameId).
        addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val catches = snapshot.data?.get("catches") as ArrayList<HashMap<String, String>>
                catchList.clear()
                for(catch in catches){
                    if(catch["catcher"] != "starter"){
                        catchList.add(catch)
                    }
                }
                catchAdapter.notifyDataSetChanged()
            }
        }
    }
}

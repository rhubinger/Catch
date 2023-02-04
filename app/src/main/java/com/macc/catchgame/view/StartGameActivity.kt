package com.macc.catchgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.macc.catchgame.R
import com.macc.catchgame.control.StartGameUserAdapter

class StartGameActivity : AppCompatActivity() {

    private lateinit var buttonStartGame : Button
    private lateinit var recyclerView: RecyclerView

    private val itemsList = ArrayList<String>()
    private lateinit var userAdapter: StartGameUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        buttonStartGame = findViewById(R.id.buttonStartGame)
        recyclerView = findViewById(R.id.recyclerViewStartGame)

        buttonStartGame.setOnClickListener {
            var intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        userAdapter = StartGameUserAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter
        prepareItems()
    }

    private fun prepareItems() {
        itemsList.add("Alan Turing")
        itemsList.add("Konrad Zuse")
        itemsList.add("John Backus")
        itemsList.add("Ada Lovelace")
        itemsList.add("Edsger Dijkstra")
        userAdapter.notifyDataSetChanged()
    }
}

package com.macc.catchgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.macc.catchgame.R
import com.macc.catchgame.control.CatchAdapter

class ResultActivity : AppCompatActivity() {

    private lateinit var buttonFinish: Button

    private val itemsList = ArrayList<String>()
    private lateinit var catchAdapter: CatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        buttonFinish = findViewById(R.id.buttonFinish)

        buttonFinish.setOnClickListener {
            var intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewResult)
        catchAdapter = CatchAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = catchAdapter
        prepareItems()
    }


    private fun prepareItems() {
        itemsList.add("Konrad Zuse caught Alan Turing")
        itemsList.add("Alan Turing caught Konrad Zuse")
        itemsList.add("Konrad Zuse caught Edsger Dijkstra")
        itemsList.add("Konrad Zuse caught Ada Lovelace")
        itemsList.add("Ada Lovelace caught John Backus")
        catchAdapter.notifyDataSetChanged()
    }
}
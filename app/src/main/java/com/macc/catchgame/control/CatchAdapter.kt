package com.macc.catchgame.control

import android.util.Log
import com.macc.catchgame.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class CatchAdapter(private var itemsList: List<String>) :
    RecyclerView.Adapter<CatchAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.textViewUserList)
        var buttonUserList: Button = view.findViewById(R.id.buttonUserList)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_user, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemTextView.text = item
        holder.buttonUserList.text = "Proof"
        holder.buttonUserList.setOnClickListener { v ->
            Log.d("MOCK", "Show the proof of the catch")
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}
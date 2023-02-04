package com.macc.catchgame.adapters

import com.macc.catchgame.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class CatchAdapter(private var itemsList: List<HashMap<String, String>>) :
    RecyclerView.Adapter<CatchAdapter.MyViewHolder>() {

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.textViewUserList)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_catch, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val catcher = itemsList[position]["catcher"]
        val caught = itemsList[position]["caught"]
        holder.itemTextView.text = "$catcher caught\n$caught"
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}
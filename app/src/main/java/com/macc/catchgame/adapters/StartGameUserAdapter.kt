package com.macc.catchgame.adapters

import com.macc.catchgame.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

internal class StartGameUserAdapter(private var itemsList: List<String>, private var gameId: String) :
    RecyclerView.Adapter<StartGameUserAdapter.MyViewHolder>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.textViewUserList)
        var buttonKickPlayer: Button = view.findViewById(R.id.buttonUserList)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_user, parent, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val username = itemsList[position]
        holder.itemTextView.text = username
        holder.buttonKickPlayer.text = "Kick"

        if(auth.currentUser?.email.toString() == username) {
            holder.buttonKickPlayer.isEnabled = false
            holder.buttonKickPlayer.visibility = View.INVISIBLE
        }

        holder.buttonKickPlayer.setOnClickListener { v ->
            val game = db.collection("games").document(gameId)
            game.update("players", FieldValue.arrayRemove(username))
                .addOnSuccessListener {
                    Toast.makeText(v.context, "Kicked user $username", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(v.context, "Failed to kick user", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}
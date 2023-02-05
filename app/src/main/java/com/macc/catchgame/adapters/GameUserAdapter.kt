package com.macc.catchgame.adapters

import android.content.Intent
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
import com.macc.catchgame.activities.GameActivity
import com.macc.catchgame.activities.MenuActivity

internal class GameUserAdapter(private var itemsList: List<String>, private var gameId: String) :
    RecyclerView.Adapter<GameUserAdapter.MyViewHolder>() {

    private var userIsCatcher: Boolean = false

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.textViewUserList)
        var buttonCatchPlayer: Button = view.findViewById(R.id.buttonUserList)
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
        holder.buttonCatchPlayer.text = "Catch"
        holder.buttonCatchPlayer.isEnabled = false
        holder.buttonCatchPlayer.visibility = View.INVISIBLE

        if(auth.currentUser?.email.toString() != username) {
            db.collection("games").document(gameId).
            addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val catchers = snapshot.data?.get("catchers") as ArrayList<String>
                    if(catchers.contains(auth.currentUser?.email.toString())){
                        userIsCatcher = true
                        holder.buttonCatchPlayer.isEnabled = true
                        holder.buttonCatchPlayer.visibility = View.VISIBLE
                    }
                }
            }
        }

        holder.buttonCatchPlayer.setOnClickListener { v ->
            val game = db.collection("games").document(gameId)
            game.update("catches", FieldValue.arrayUnion(hashMapOf(
                "catcher" to auth.currentUser?.email.toString(),
                "caught" to username,
            ))).addOnFailureListener {
                    Toast.makeText(v.context, "Catch failed", Toast.LENGTH_SHORT).show()
                }
            game.update("catchers", FieldValue.arrayUnion(username)).addOnFailureListener {
                Toast.makeText(v.context, "Role change failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}
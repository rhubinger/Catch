package com.macc.catchgame.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.macc.catchgame.R

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextEmail : EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginBtn : Button
    private lateinit var toRegistration : TextView

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            var intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.username)
        editTextPassword = findViewById(R.id.password)
        loginBtn = findViewById(R.id.buttonLogin)
        toRegistration = findViewById(R.id.toRegistration)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener { view : View ->
            var email = editTextEmail.text.toString()
            var password = editTextPassword.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this, "Email field can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this, "Password field can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        var intent = Intent(applicationContext, MenuActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        toRegistration.setOnClickListener {
            var intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

}
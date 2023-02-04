package com.macc.catchgame.activities

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

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail : EditText
    private lateinit var editTextPassword: EditText
    private lateinit var registerBtn : Button
    private lateinit var toLogin : TextView

    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            var intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextEmail = findViewById(R.id.username)
        editTextPassword = findViewById(R.id.password)
        registerBtn = findViewById(R.id.buttonRegister)
        toLogin = findViewById(R.id.toLogin)

        auth = FirebaseAuth.getInstance()

        registerBtn.setOnClickListener { view : View ->
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

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, "Account created",
                            Toast.LENGTH_SHORT).show()
                        var intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Account creation failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        toLogin.setOnClickListener {
            var intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}
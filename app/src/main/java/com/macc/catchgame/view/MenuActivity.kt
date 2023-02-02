package com.macc.catchgame.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.macc.catchgame.databinding.ActivityMainBinding


class MenuActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
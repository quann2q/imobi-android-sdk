package com.n2q.sdk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.n2q.sdk.R
import com.n2q.sdk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}
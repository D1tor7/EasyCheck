package com.example.easycheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.easycheck.databinding.ActivityMainBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
private lateinit var auth: FirebaseAuth
private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
    }
}
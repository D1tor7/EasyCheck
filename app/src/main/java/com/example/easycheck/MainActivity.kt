package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.easycheck.controlador.InformationActivity
import com.example.easycheck.controlador.ProfileActivity
import com.example.easycheck.controlador.RoomActivity
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

        binding.profileImageView.setOnClickListener{
            val intent=Intent(this, ProfileActivity::class.java)
            startActivity( intent)
        }
        binding.buttonHabitacion.setOnClickListener{
            val intent=Intent(this, RoomActivity::class.java)
            startActivity(intent)
        }
        binding.buttonInformacion.setOnClickListener{
            val intent=Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }
        binding.buttonMisreservas.setOnClickListener{
            val intent=Intent(this, PagoActivity::class.java)
            startActivity(intent)
        }

    }


}
package com.example.easycheck.Controlador

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.easycheck.Controlador.Login.SignInActivity
import com.example.easycheck.R
import com.example.easycheck.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth

        binding.signOutImageView.setOnClickListener{
            signOut()
        }

        showUserName()

        binding.backImageView.setOnClickListener{
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showUserName() {
        val user = auth.currentUser

        if (user != null) {
            binding.emailTextView.text = user.email

            // Get the user document from Firestore
            val db = Firebase.firestore
            val userEmail = user.email
            val userRef = db.collection("users").whereEqualTo("email", userEmail)

            userRef.get().addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the first document with the matching email address
                    val document = querySnapshot.documents[0]
                    val name = document.getString("name")
                    binding.nameTextView.text = name
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error getting user document: ", exception)
                Toast.makeText(this, "Error obteniendo información del usuario", Toast.LENGTH_SHORT).show()
            }

            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.profileImageView)

            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.bgProfileImageView)
        }
    }

    private fun signOut(){
        Firebase.auth.signOut()
        val intent= Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }


}
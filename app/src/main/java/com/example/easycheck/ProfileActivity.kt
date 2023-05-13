package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.easycheck.databinding.ActivityMainBinding
import com.example.easycheck.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
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

        updateUI()


        binding.backImageView.setOnClickListener{
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.updateProfileAppCompatButton.setOnClickListener {
            val name=binding.nameEditText.text.toString()
            val dni=binding.dniEditText.text.toString()
            updateProfile(name, dni)
        }
    }

    private fun updateProfile(name:String,dni:String){
        val user =auth.currentUser

        val profileUpdates= userProfileChangeRequest {
            displayName=name
        }

        user!!.updateProfile(profileUpdates).addOnCompleteListener{task->
            if(task.isSuccessful){
                val userUpdates = hashMapOf<String, Any>(
                    "dni" to dni)
                //Actualiza el DNI en Firestore
                Firebase.firestore.collection("users").document(user.uid)
                    .update(userUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext,"Se realizaron los cambios correctamente",
                            Toast.LENGTH_SHORT).show()
                        updateUI()
                    }.addOnFailureListener {
                        Toast.makeText(baseContext,"Error al actualizar el DNI",
                            Toast.LENGTH_SHORT).show()
                    }



            }else{
                Toast.makeText(baseContext,"Error al actualizar el nombre",
                    Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun signOut(){
        Firebase.auth.signOut()
        val intent= Intent(this,SignInActivity::class.java)
        startActivity(intent)
    }
    private fun updateUI(){
        val user=auth.currentUser

        if (user!=null){
            binding.emailTextView.text=user.email
            binding.nameTextView.text=user.displayName
            binding.nameEditText.setText(user.displayName)

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
}
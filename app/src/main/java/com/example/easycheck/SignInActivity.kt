package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.easycheck.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signInAppCompatButton.setOnClickListener{
            val mEmail=binding.emailEditText.text.toString()
            val mPassword=binding.passwordEditText.text.toString()

            when{
                mEmail.isEmpty()|| mPassword.isEmpty()->{
                    Toast.makeText(baseContext,"Correo o contraseña incorrectos",
                    Toast.LENGTH_SHORT).show()
                }else  ->{
                SignIn(mEmail,mPassword)
                }
            }

        }
    }

    private fun SignIn(email : String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    reaload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Correo o contraseña incorrectos",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun reaload (){
        val intent =Intent(this,MainActivity::class.java)
        this.startActivity(intent)
    }
}
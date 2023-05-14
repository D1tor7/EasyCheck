package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.easycheck.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import javax.annotation.Nonnull

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

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
        binding.signUpTextView.setOnClickListener{
            val intent=Intent(this, SignUpActivity::class.java)
            startActivity( intent)
        }

        binding.recoveryAccountTextView.setOnClickListener{
            val intent=Intent(this, AccountRecoveryActivity::class.java)
            startActivity( intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified){
                reload()
            }else{
                val intent=Intent(this,CheckEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userID = user!!.uid
                    val documentReference: DocumentReference =
                        firestore.collection("users").document(userID)

                    // Actualizar los datos de inicio de sesión del usuario en el mismo documento
                    documentReference.update(mapOf(
                        "email" to email,
                        "password" to password
                    )).addOnSuccessListener {
                        Log.d("TAG", "User data updated in Firestore")
                    }.addOnFailureListener { e ->
                        Log.e("TAG", "Error updating user data in Firestore", e)
                    }

                    if (user.isEmailVerified) {
                        Log.d("TAG", "signInWithEmail:success")
                        reload()
                    } else {
                        val intent = Intent(this, CheckEmailActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // Si falla el inicio de sesión, mostrar un mensaje al usuario.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Correo o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }



    private fun reload (){
        val intent =Intent(this,MainActivity::class.java)
        this.startActivity(intent)
    }
}
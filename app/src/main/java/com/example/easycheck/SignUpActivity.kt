package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.easycheck.databinding.ActivitySignInBinding
import com.example.easycheck.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener{
            val mEmail=binding.emailEditText.text.toString()
            val mPassword=binding.passwordEditText.text.toString()
            val mrepeatPassword=binding.repeatPasswordEditText.text.toString()

             val passwordRegex=Pattern.compile(
                 ".{6,}"+"$")//tener almenos 6 caracteres

            if (mEmail.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                Toast.makeText(baseContext, "Ingrese un Email Valido.", Toast.LENGTH_SHORT,).show()
            }else if(mPassword.isEmpty()|| !passwordRegex.matcher(mPassword).matches()){
                Toast.makeText(baseContext, "La contraseña es debil", Toast.LENGTH_SHORT,).show()
            }else if (mPassword !=mrepeatPassword){
                Toast.makeText(baseContext, "Confirma la contraseña", Toast.LENGTH_SHORT,).show()
            }else{
                createAccount(mEmail,mPassword)
                Toast.makeText(baseContext, "Cuenta Creada Satisfactoriamente", Toast.LENGTH_SHORT,).show()
            }
            createAccount(mEmail,mPassword)
        }

        binding.backImageView.setOnClickListener{
            val intent=Intent(this,SignInActivity::class.java)
            startActivity(intent)
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

    private fun createAccount(email: String,password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent=Intent(this,CheckEmailActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()

                }
            }
    }
    private fun reload (){
        val intent =Intent(this,MainActivity::class.java)
        this.startActivity(intent)
    }
}
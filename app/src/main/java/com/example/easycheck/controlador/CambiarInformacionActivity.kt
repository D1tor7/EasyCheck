package com.example.easycheck.controlador

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.easycheck.databinding.ActivityCambiarInformacionBinding
import com.example.easycheck.modelo.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CambiarInformacionActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCambiarInformacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambiarInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.saveButton.setOnClickListener {
            val mName = binding.nameEditText.text.toString()
            val mDni = binding.dniEditText.text.toString()
            val mCellPhone = binding.cellphoneEditText.text.toString()

            // Guardar los cambios en Firestore llamando a una función de actualización
            updateUserInformation(mName, mDni, mCellPhone)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Verificar que el usuario está conectado y actualizar el UI
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Obtener los datos actuales del usuario desde Firestore
            val userDocumentRef = Firebase.firestore.collection("users").document(currentUser.email!!)
            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(UserData::class.java)
                        // Mostrar los datos actuales en los campos correspondientes
                        showUserData(user)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al obtener los datos del usuario", e)
                }
        }
    }

    private fun updateUserInformation(name: String, dni: String, cellPhone: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Actualizar los campos necesarios en Firestore
            val userDocumentRef = Firebase.firestore.collection("users").document(currentUser.email!!)
            userDocumentRef.update(
                "name", name,
                "dni", dni,
                "cellphone", cellPhone
            )
                .addOnSuccessListener {
                    Toast.makeText(this, "Información actualizada correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Regresar a la actividad anterior después de guardar los cambios
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al actualizar la información del usuario", e)
                    Toast.makeText(this, "Error al actualizar la información", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showUserData(user: UserData?) {
        // Asignar los datos del usuario a los campos correspondientes
        binding.nameEditText.setText(user?.name)
        binding.dniEditText.setText(user?.dni)
        binding.cellphoneEditText.setText(user?.cellphone)
    }
}
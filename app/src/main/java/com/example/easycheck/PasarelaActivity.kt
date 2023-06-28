package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class PasarelaActivity : AppCompatActivity() {

    private lateinit var numeroTarjetaEditText: EditText
    private lateinit var nombreTitularEditText: EditText
    private lateinit var fechaVencimientoEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var pagarButton: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser
    private lateinit var reservasCollection: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasarela)

        db = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!
        reservasCollection = db.collection("reservas")

        numeroTarjetaEditText = findViewById(R.id.numeroTarjetaEditText)
        nombreTitularEditText = findViewById(R.id.nombreTitularEditText)
        fechaVencimientoEditText = findViewById(R.id.fechaVencimientoEditText)
        cvvEditText = findViewById(R.id.cvvEditText)
        pagarButton = findViewById(R.id.pagarButton)

        pagarButton.setOnClickListener {
            val numeroTarjeta = numeroTarjetaEditText.text.toString().trim()
            val nombreTitular = nombreTitularEditText.text.toString().trim()
            val fechaVencimiento = fechaVencimientoEditText.text.toString().trim()
            val cvv = cvvEditText.text.toString().trim()

            if (numeroTarjeta.isNotEmpty() && nombreTitular.isNotEmpty() && fechaVencimiento.isNotEmpty() && cvv.isNotEmpty()) {
                // Emular el proceso de pago
                // Simular un retraso para emular el procesamiento del pago
                pagarButton.isEnabled = false
                Handler().postDelayed({
                    // Obtener el correo electrónico del usuario actual
                    val emailUsuarioActual = currentUser.email

                    if (emailUsuarioActual != null) {
                        // Obtener el documento de reserva del usuario actual utilizando el correo electrónico
                        val reservaQuery = reservasCollection.whereEqualTo("user", emailUsuarioActual).limit(1)

                        reservaQuery.get()
                            .addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    val reservaDocument = querySnapshot.documents[0]
                                    val documentoUsuarioActual = reservaDocument.id

                                    // Actualizar el campo "pagado" en el documento de Firestore
                                    val reservaDocumentRef = reservasCollection.document(documentoUsuarioActual)
                                    reservaDocumentRef.update("pagado", true)
                                        .addOnSuccessListener {
                                            mostrarPopup("Pago realizado exitosamente") {
                                                // Redirigir a MainActivity
                                                val intent = Intent(this@PasarelaActivity, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                            pagarButton.isEnabled = true
                                        }
                                        .addOnFailureListener { exception ->
                                            mostrarPopup("Error al procesar el pago")
                                            pagarButton.isEnabled = true
                                        }
                                } else {
                                    mostrarPopup("No se encontró una reserva para el usuario actual")
                                    pagarButton.isEnabled = true
                                }
                            }
                            .addOnFailureListener { exception ->
                                mostrarPopup("Error al obtener la reserva del usuario actual")
                                pagarButton.isEnabled = true
                            }
                    } else {
                        mostrarPopup("No se pudo obtener el correo electrónico del usuario actual")
                        pagarButton.isEnabled = true
                    }
                }, 2000)
            } else {
                mostrarPopup("Por favor, complete todos los campos")
            }
        }
    }

    private fun mostrarPopup(mensaje: String, accion: () -> Unit = {}) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(mensaje)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                accion()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
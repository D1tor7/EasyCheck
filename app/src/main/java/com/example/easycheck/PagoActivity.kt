package com.example.easycheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class PagoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var emailcurrentuserTextView: TextView
    private lateinit var ndiasTextView: TextView
    private lateinit var costoTextView: TextView
    private lateinit var habitacionesTextView: TextView
    private lateinit var cancelarButton: Button
    private lateinit var pagarButton: Button

    private lateinit var reservaUsuarioActual: ReservaData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        emailcurrentuserTextView = findViewById(R.id.emailcurrentuser_textview)
        ndiasTextView = findViewById(R.id.ndias_textview)
        costoTextView = findViewById(R.id.costo_textview)
        habitacionesTextView = findViewById(R.id.habitaciones_textview)
        cancelarButton = findViewById(R.id.cancelar_button)
        pagarButton = findViewById(R.id.pagar_button)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val usersCollection = db.collection("users")
        val reservasCollection = db.collection("reservas")

        usersCollection.whereEqualTo("email", currentUser?.email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val userIdDocumento = userDocument.id

                    // Obtener la reserva del usuario actual utilizando el ID de documento del usuario
                    reservasCollection.document(userIdDocumento)
                        .get()
                        .addOnSuccessListener { reservaSnapshot ->
                            if (reservaSnapshot.exists()) {
                                val ndias = reservaSnapshot.getString("Ndias")
                                val pagado = reservaSnapshot.getBoolean("pagado")
                                val costo = reservaSnapshot.getDouble("costo")
                                val habitaciones = reservaSnapshot.getString("habitaciones")
                                val user = currentUser?.email

                                reservaUsuarioActual = ReservaData(ndias, pagado, costo, habitaciones, user)

                                // Actualizar las vistas con la información de la reserva
                                emailcurrentuserTextView.text = reservaUsuarioActual.user
                                ndiasTextView.text = reservaUsuarioActual.ndias
                                costoTextView.text = reservaUsuarioActual.costo.toString()
                                habitacionesTextView.text = reservaUsuarioActual.habitaciones

                                // Actualizar el estado en base a la variable 'pagado'
                                val estadoTextView = findViewById<TextView>(R.id.estado_textview)
                                estadoTextView.text = if (reservaUsuarioActual.pagado == true) "Pagado" else "No pagado"
                                pagarButton.isEnabled = reservaUsuarioActual.pagado != true
                            } else {
                                // No se encontró una reserva para el usuario actual
                                mostrarPopup("No tiene reservas efectuadas")

                            }
                        }
                        .addOnFailureListener { exception ->
                            // Manejar cualquier error en caso de que la consulta falle
                        }
                } else {
                    // No se encontró un usuario con el correo electrónico actual
                }
            }
            .addOnFailureListener { exception ->
                // Manejar cualquier error en caso de que la consulta falle
            }

        cancelarButton.setOnClickListener {
            // Eliminar la reserva del Firestore
            usersCollection.whereEqualTo("email", currentUser?.email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val userDocument = querySnapshot.documents[0]
                        val userIdDocumento = userDocument.id

                        // Obtener la reserva del usuario actual utilizando el ID de documento del usuario
                        reservasCollection.document(userIdDocumento)
                            .get()
                            .addOnSuccessListener { reservaSnapshot ->
                                if (reservaSnapshot.exists()) {
                                    val ndias = reservaSnapshot.getString("Ndias")
                                    val pagado = reservaSnapshot.getBoolean("pagado")
                                    val costo = reservaSnapshot.getDouble("costo")
                                    val habitaciones = reservaSnapshot.getString("habitaciones")
                                    val user = currentUser?.email

                                    reservaUsuarioActual = ReservaData(ndias, pagado, costo, habitaciones, user)

                                    // Actualizar las vistas con la información de la reserva
                                    emailcurrentuserTextView.text = reservaUsuarioActual.user
                                    ndiasTextView.text = reservaUsuarioActual.ndias
                                    costoTextView.text = reservaUsuarioActual.costo.toString()
                                    habitacionesTextView.text = reservaUsuarioActual.habitaciones

                                    // Actualizar el estado en base a la variable 'pagado'
                                    val estadoTextView = findViewById<TextView>(R.id.estado_textview)
                                    estadoTextView.text = if (reservaUsuarioActual.pagado == true) "Pagado" else "No pagado"

                                    // Actualizar el estado de las habitaciones reservadas a 'true' en la colección "rooms"
                                    val habitacionesReservadas = habitaciones?.split(",")?.map { it.trim() } ?: listOf()
                                    val roomsCollection = db.collection("rooms")

                                    for (habitacionId in habitacionesReservadas) {
                                        roomsCollection.document(habitacionId)
                                            .update("Disponibilidad", true)
                                            .addOnSuccessListener {
                                                // Estado de la habitación actualizado exitosamente
                                            }
                                            .addOnFailureListener { exception ->
                                                // Manejar cualquier error en caso de que falle la actualización
                                            }
                                    }

                                    // Eliminar la reserva del usuario actual utilizando el ID de documento del usuario
                                    reservasCollection.document(userIdDocumento)
                                        .delete()
                                        .addOnSuccessListener {
                                            // Reserva eliminada exitosamente
                                            mostrarPopup("Reserva cancelada") {
                                                // Redirigir al MainActivity
                                                val intent = Intent(this, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            // Manejar cualquier error en caso de que falle la eliminación
                                        }
                                } else {
                                    // No se encontró una reserva para el usuario actual
                                    mostrarPopup("No tiene reservas efectuadas")
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Manejar cualquier error en caso de que la consulta falle
                            }
                    } else {
                        // No se encontró un usuario con el correo electrónico actual
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar cualquier error en caso de que la consulta falle
                }
        }

        pagarButton.setOnClickListener {
            // Redirigir a la actividad PasarelaActivity
            val intent = Intent(this, PasarelaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mostrarPopup(mensaje: String, accion: () -> Unit = {}) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(mensaje)
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                accion()
                // Redirigir al MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
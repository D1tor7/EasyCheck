package com.example.easycheck.Controlador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.easycheck.R
import com.google.firebase.firestore.FirebaseFirestore


class ReservaActivity : FragmentActivity() {
    private lateinit var habitacionesSeleccionadas: List<String>
    private lateinit var habitacionesTextView: TextView
    private lateinit var precioTextView: TextView
    private lateinit var diasSpinner: Spinner
    private lateinit var diasTextView: TextView
    private lateinit var reservarButton: Button

    private var diasReserva: Int = 0

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva)

        habitacionesTextView = findViewById(R.id.habitaciones_textview)
        precioTextView = findViewById(R.id.precio_textview)
        diasSpinner = findViewById(R.id.dias_reserva_spinner)
        diasTextView = findViewById(R.id.dias_textview)
        reservarButton = findViewById(R.id.reservar_button)

        habitacionesSeleccionadas = intent.getStringArrayListExtra("habitacionesSeleccionadas") ?: emptyList()
        mostrarResumenReserva()

        val diasAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.dias_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            diasSpinner.adapter = adapter
        }

        diasSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                diasReserva = parent?.getItemAtPosition(position).toString().toInt()
                calcularPrecio()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario realizar ninguna acción
            }
        }

        reservarButton.setOnClickListener {
            crearReserva()
        }
    }

    private fun mostrarResumenReserva() {
        val habitacionesText = StringBuilder()
        for (habitacionId in habitacionesSeleccionadas) {
            habitacionesText.append(habitacionId).append(", ")
        }

        if (habitacionesText.length >= 2) {
            habitacionesText.delete(habitacionesText.length - 2, habitacionesText.length)
        }

        habitacionesTextView.text = habitacionesText.toString()
    }

    private fun calcularPrecio() {
        val precioHabitacion = obtenerPrecioHabitacion()
        val precioTotal = precioHabitacion * habitacionesSeleccionadas.size * diasReserva
        precioTextView.text = getString(R.string.price_format, precioTotal)
    }

    private fun obtenerPrecioHabitacion(): Double {
        // Aquí puedes obtener el precio de la habitación desde donde sea que lo almacenes (por ejemplo, desde SharedPreferences)
        return 90.0
    }

    private fun crearReserva() {
        val ndias = diasReserva.toString()
        val costo = obtenerPrecioHabitacion() * habitacionesSeleccionadas.size * diasReserva
        val habitaciones = habitacionesSeleccionadas.joinToString(", ")

        val reservaData = hashMapOf(
            "Ndias" to ndias,
            "costo" to costo,
            "habitaciones" to habitaciones
        )

        db.collection("reservas")
            .add(reservaData)
            .addOnSuccessListener { documentReference ->
                // La reserva se agregó exitosamente a Firestore
                mostrarDialogoReservaExitosa()
                actualizarDisponibilidadHabitaciones()
            }
            .addOnFailureListener { exception ->
                // Manejar errores al agregar la reserva a Firestore
                mostrarDialogoErrorReserva()
            }
    }
    private fun mostrarDialogoReservaExitosa() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Reserva exitosa")
        dialogBuilder.setMessage("La reserva se realizó exitosamente.")
        dialogBuilder.setPositiveButton("Aceptar") { _, _ ->
            // Redirigir al usuario al MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerrar la actividad actual
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun mostrarDialogoErrorReserva() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Error")
        dialogBuilder.setMessage("Error al realizar la reserva.")
        dialogBuilder.setPositiveButton("Aceptar", null)
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun actualizarDisponibilidadHabitaciones() {
        for (habitacionId in habitacionesSeleccionadas) {
            val habitacionRef = db.collection("rooms").document(habitacionId)

            habitacionRef
                .update("Disponibilidad", false)
                .addOnSuccessListener {
                    // La disponibilidad de la habitación se actualizó exitosamente
                    Toast.makeText(this, "Disponibilidad de habitación actualizada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Manejar errores al actualizar la disponibilidad de la habitación
                    Toast.makeText(this, "Error al actualizar la disponibilidad de habitación", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
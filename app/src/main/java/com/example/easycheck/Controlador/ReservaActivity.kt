package com.example.easycheck.Controlador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.easycheck.R
import com.example.easycheck.Modelo.RoomAdapter
import com.example.easycheck.Modelo.RoomData
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class ReservaActivity : AppCompatActivity() {
    private lateinit var habitacionesSeleccionadas: List<RoomData>
    private lateinit var fechaIngreso: String
    private lateinit var fechaSalida: String

    private lateinit var habitacionesTextView: TextView
    private lateinit var precioTextView: TextView
    private lateinit var diasTextView: TextView
    private lateinit var pagarButton: Button

    private lateinit var roomAdapter: RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva)

        habitacionesTextView = findViewById(R.id.habitaciones_textview)
        precioTextView = findViewById(R.id.precio_textview)
        diasTextView = findViewById(R.id.dias_textview)
        pagarButton = findViewById(R.id.pagar_button)

        roomAdapter = RoomAdapter()

        habitacionesSeleccionadas = roomAdapter.getHabitacionesSeleccionadas()
        fechaIngreso = intent.getStringExtra("fechaIngreso") ?: ""
        fechaSalida = intent.getStringExtra("fechaSalida") ?: ""

        mostrarResumenReserva()
        calcularPrecio()
        calcularDias()

        pagarButton.setOnClickListener {
            // Realizar el pago y actualizar la disponibilidad de las habitaciones seleccionadas en Firebase
            val firestore = FirebaseFirestore.getInstance()

            // Obtener una referencia a la colección "reservas"
            val reservasCollection = firestore.collection("reservas")

            // Crear un objeto Reserva con los datos necesarios
            val reserva = hashMapOf(
                "costo" to calcularPrecioTotal(),
                "fecha de entrada" to fechaIngreso,
                "fecha de salida" to fechaSalida,
                "hora de entrada" to "13:00",
                "hora de salida" to "12:00",
                "habitaciones" to obtenerIdsHabitaciones()
            )

            // Agregar la reserva a la colección "reservas"
            reservasCollection.add(reserva)
                .addOnSuccessListener { documentReference ->
                    // La reserva se ha agregado correctamente
                    val reservaId = documentReference.id
                    // Realizar acciones adicionales si es necesario
                }
                .addOnFailureListener { exception ->
                    // Error al agregar la reserva
                    // Manejar el error adecuadamente
                }
        }
    }

    private fun mostrarResumenReserva() {
        val habitacionesText = StringBuilder()
        for (habitacion in habitacionesSeleccionadas) {
            habitacionesText.append(habitacion.id).append(", ")
        }

        if (habitacionesText.length >= 2) {
            habitacionesText.delete(habitacionesText.length - 2, habitacionesText.length) // Eliminar la última coma y espacio
        }

        habitacionesTextView.text = habitacionesText.toString()
    }

    private fun calcularPrecio() {
        val precioTotal = calcularPrecioTotal()
        precioTextView.text = getString(R.string.price_format, precioTotal)
    }

    private fun calcularPrecioTotal(): Double {
        var precioTotal = 0.0
        for (habitacion in habitacionesSeleccionadas) {
            precioTotal += habitacion.Precio
        }
        return precioTotal
    }

    private fun calcularDias() {
        if (fechaIngreso.isNotEmpty() && fechaSalida.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val fechaIngresoDate = dateFormat.parse(fechaIngreso)
                val fechaSalidaDate = dateFormat.parse(fechaSalida)

                val daysInMillis = fechaSalidaDate.time - fechaIngresoDate.time
                val days = TimeUnit.MILLISECONDS.toDays(daysInMillis)

                diasTextView.text = days.toString()
            } catch (e: ParseException) {
                // Handle the ParseException appropriately
                e.printStackTrace()
            }
        } else {
            // Handle the case where fechaIngreso or fechaSalida is empty or null
        }
    }

    private fun obtenerIdsHabitaciones(): String {
        val idsHabitaciones = StringBuilder()
        for (habitacion in habitacionesSeleccionadas) {
            idsHabitaciones.append(habitacion.id).append(", ")
        }
        idsHabitaciones.delete(idsHabitaciones.length - 2, idsHabitaciones.length) // Eliminar la última coma y espacio
        return idsHabitaciones.toString()
    }
}
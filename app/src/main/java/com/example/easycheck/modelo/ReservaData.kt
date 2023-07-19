package com.example.easycheck.modelo

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
data class ReservaData(
    val ndias: String?,
    val pagado: Boolean?,
    val costo: Double?,
    val habitaciones: String?,
    val user: String?,
    var claveUnica: String? // Agregar el campo claveUnica al modelo
)
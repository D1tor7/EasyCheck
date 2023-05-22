package com.example.easycheck

data class RoomData(
    var id: String = "", // Propiedad agregada para almacenar el ID del documento
    var npiso: Double = 0.0,
    var Precio: Double = 0.0,
    var Disponibilidad: Boolean = true,
    var Informacion: String = "",

    )
package com.example.easycheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RoomFragment : Fragment() {
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var roomList: RecyclerView
    private lateinit var reservarButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        val view = inflater.inflate(R.layout.fragment_room, container, false)

        roomList = view.findViewById(R.id.room_list)
        roomList.layoutManager = LinearLayoutManager(requireContext())
        roomAdapter = RoomAdapter()
        roomList.adapter = roomAdapter



        reservarButton = view.findViewById(R.id.reservar_button)
        reservarButton.setOnClickListener {
            val habitacionesSeleccionadas = roomAdapter.getHabitacionesSeleccionadas()
            // Realizar la reserva con las habitaciones seleccionadas
            for (habitacion in habitacionesSeleccionadas) {
                // Realizar las acciones necesarias con cada habitación seleccionada
                val habitacionId = habitacion.id
                val habitacionNpiso = habitacion.npiso
                val habitacionPrecio = habitacion.Precio
                val habitacionInformacion = habitacion.Informacion

                // Aquí puedes realizar la lógica correspondiente para la reserva
                // según los datos de cada habitación seleccionada
            }
        }
        loadRoomsFromFirestore()

        return view
    }

    private fun loadRoomsFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val roomsCollection = firestore.collection("rooms")

        roomsCollection.get()
            .addOnSuccessListener { result ->
                val roomDataList = mutableListOf<RoomData>()
                for (document in result) {
                    val id = document.id
                    val npiso = document.getDouble("nPiso") ?: 0.0
                    val precio = document.getDouble("Precio") ?: 0.0
                    val disponibilidad = document.getBoolean("Disponibilidad") ?: true
                    val informacion = document.getString("Informacion") ?: ""

                    val roomData = RoomData(id, npiso, precio, disponibilidad, informacion)
                    roomDataList.add(roomData)
                }
                roomAdapter.setRooms(roomDataList)
            }
            .addOnFailureListener { exception ->
                // Manejar errores al obtener los datos de Firestore
            }
    }

}
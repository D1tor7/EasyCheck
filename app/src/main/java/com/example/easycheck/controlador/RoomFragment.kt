package com.example.easycheck.controlador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easycheck.modelo.RoomAdapter
import com.example.easycheck.modelo.RoomData
import com.example.easycheck.R
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
        roomAdapter = RoomAdapter(requireContext(), this)

        roomList.adapter = roomAdapter



        reservarButton = view.findViewById(R.id.reservar_button)
        reservarButton.setOnClickListener {
            roomAdapter.sendSelectedRooms()
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
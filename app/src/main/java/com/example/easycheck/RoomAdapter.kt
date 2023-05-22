package com.example.easycheck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomAdapter : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    private val rooms: MutableList<RoomData> = mutableListOf()
    private val habitacionesSeleccionadas = mutableListOf<RoomData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    fun setRooms(newRooms: List<RoomData>) {
        rooms.clear()
        rooms.addAll(newRooms)
        notifyDataSetChanged()
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTextView: TextView = itemView.findViewById(R.id.room_id)
        private val npisoTextView: TextView = itemView.findViewById(R.id.room_npiso)
        private val precioTextView: TextView = itemView.findViewById(R.id.room_precio)
        private val disponibilidadCheckBox: CheckBox = itemView.findViewById(R.id.room_disponibilidad)
        private val informacionTextView: TextView = itemView.findViewById(R.id.room_informacion)
        private val disponibilidadTextView: TextView = itemView.findViewById(R.id.disponibilidadTextView)

        private lateinit var currentRoom: RoomData

        init {
            disponibilidadCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    habitacionesSeleccionadas.add(currentRoom)
                } else {
                    habitacionesSeleccionadas.remove(currentRoom)
                }
            }
        }

        fun bind(room: RoomData) {
            currentRoom = room

            idTextView.text = room.id
            npisoTextView.text = room.npiso.toString()
            precioTextView.text = room.Precio.toString()
            disponibilidadTextView.text = if (room.Disponibilidad) "Disponible" else "No disponible"
            disponibilidadCheckBox.isChecked = false
            disponibilidadCheckBox.isEnabled = room.Disponibilidad
            informacionTextView.text = room.Informacion
        }
    }

    fun getHabitacionesSeleccionadas(): List<RoomData> {
        return habitacionesSeleccionadas
    }
}
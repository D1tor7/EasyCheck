package com.example.easycheck.modelo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.easycheck.ReservaActivity
import com.example.easycheck.R


class RoomAdapter(private val context: Context, private val fragment: Fragment) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>()   {
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
        private val context = itemView.context
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

    fun sendSelectedRooms() {
        val intent = Intent(fragment.requireContext(), ReservaActivity::class.java)
        val selectedRoomIds = habitacionesSeleccionadas.map { it.id }
        intent.putStringArrayListExtra("habitacionesSeleccionadas", ArrayList(selectedRoomIds))
        fragment.startActivity(intent)
    }

}
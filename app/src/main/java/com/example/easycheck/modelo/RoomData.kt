package com.example.easycheck.modelo

import android.os.Parcel
import android.os.Parcelable

data class RoomData(
    val id: String,
    val npiso: Double,
    val Precio: Double,
    val Disponibilidad: Boolean,
    val Informacion: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeDouble(npiso)
        parcel.writeDouble(Precio)
        parcel.writeByte(if (Disponibilidad) 1 else 0)
        parcel.writeString(Informacion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomData> {
        override fun createFromParcel(parcel: Parcel): RoomData {
            return RoomData(parcel)
        }

        override fun newArray(size: Int): Array<RoomData?> {
            return arrayOfNulls(size)
        }
    }
}
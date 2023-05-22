package com.example.easycheck


import android.os.Bundle
import com.example.easycheck.RoomFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RoomFragment())
            .commit()
    }
}
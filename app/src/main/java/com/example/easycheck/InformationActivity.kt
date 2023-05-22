package com.example.easycheck

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class InformationActivity : AppCompatActivity() {


    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        // Obtain the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        mapFragment.getMapAsync(MapReadyCallback())

        // Button to open the map
        val mapButton = findViewById<Button>(R.id.map_button)
        mapButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.google.com/maps?q=hotel+name")
            startActivity(intent)
        }

        // Manejar el evento de clic del botón de Facebook
        val facebookButton = findViewById<View>(R.id.facebookButton)
        facebookButton.setOnClickListener {
            val facebookUrl = "https://www.facebook.com/galenoinn"
            abrirEnlaceExterno(facebookUrl)
        }

        // Manejar el evento de clic del botón de Instagram
        val instagramButton = findViewById<View>(R.id.instagramButton)
        instagramButton.setOnClickListener {
            val instagramUrl = "https://www.instagram.com/hostalgalenoinn/"
            abrirEnlaceExterno(instagramUrl)
        }

        // Manejar el evento de clic del botón de TikTok
        val tiktokButton = findViewById<View>(R.id.tiktokButton)
        tiktokButton.setOnClickListener {
            val tiktokUrl = "https://www.tiktok.com/@hostalgalenoinn"
            abrirEnlaceExterno(tiktokUrl)
        }
    }

    private fun abrirEnlaceExterno(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    inner class MapReadyCallback : OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap) {
            googleMap = map

            // Set the hotel location on the map
            val hotelLocation = LatLng(-8.128596, -79.030696) // Replace with the actual hotel coordinates
            googleMap.addMarker(MarkerOptions().position(hotelLocation).title("Hotel"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelLocation, 12f))
        }
    }
}
package com.appdev.placeslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.appdev.placeslist.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val defaultLocation = LatLng(38.73240225798916, -9.160349629389367)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 8f))

        val id = intent.getIntExtra("id", 1)
        getPlaces(id)

        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this@MapsActivity, DetailActivity::class.java)
            val id = marker.tag as Int
            intent.putExtra("idplace", id)
            startActivity(intent)
        }
    }

    private fun getPlaces(id: Int) {
        val url = resources.getString(R.string.url) + id + "/"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jresponse = response.getJSONObject(0)
                    val id = jresponse.getInt("id")
                    val name = jresponse.getString("name")
                    val address = jresponse.getString("address")
                    val latitude = jresponse.getDouble("latitude")
                    val longitude = jresponse.getDouble("longitude")
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(latitude, longitude))
                            .title(name)
                            .snippet(address)
                    )
                    marker?.tag = id
                    Log.d("name", name)
                    msg(response.toString())
                } catch (je: JSONException) {
                    // Handle JSON parsing error
                }
            },
            { error ->
                // Handle error
                msg("That didn't work! \n$error")
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun msg(txt: String) {
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show()
    }
}

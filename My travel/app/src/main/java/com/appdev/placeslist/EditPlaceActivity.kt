package com.appdev.placeslist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class EditPlaceActivity : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var latitudeEditText: EditText? = null
    private var longitudeEditText: EditText? = null
    private var descriptionEditText: EditText? = null
    private var saveButton: Button? = null
    private var placeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_place)

        // Retrieve the place ID passed from the previous activity
        val extras = intent.extras
        placeId = extras?.getString("placeId")?.toIntOrNull() ?: 0

        if (placeId == 0) {
            // Handle error when place ID is not provided
            finish()
            return
        }

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        addressEditText = findViewById(R.id.addressEditText)
        latitudeEditText = findViewById(R.id.latitudeEditText)
        longitudeEditText = findViewById(R.id.longitudeEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        saveButton = findViewById(R.id.saveButton)

        // Fetch place details from the server
        fetchPlaceDetails()

        // Set click listener for the Save button
        saveButton?.setOnClickListener { savePlace() }
    }

    private fun fetchPlaceDetails() {
        val url = getString(R.string.apiplaces) + placeId
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Parse the response and populate the fields
                    val name = response.getString("name")
                    val address = response.getString("address")
                    val latitude = response.getString("latitude")
                    val longitude = response.getString("longitude")
                    val description = response.getString("description")
                    nameEditText?.setText(name)
                    addressEditText?.setText(address)
                    latitudeEditText?.setText(latitude)
                    longitudeEditText?.setText(longitude)
                    descriptionEditText?.setText(description)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { // Handle error during fetch
            Toast.makeText(
                this@EditPlaceActivity,
                "Error fetching place details",
                Toast.LENGTH_SHORT
            ).show()
        }
        Volley.newRequestQueue(this).add(request)
    }

    private fun savePlace() {
        // Retrieve input values
        val name = nameEditText?.text.toString().trim()
        val address = addressEditText?.text.toString().trim()
        val latitude = latitudeEditText?.text.toString().trim()
        val longitude = longitudeEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()

        // Create JSON object with the updated place details
        val requestBody = JSONObject()
        try {
            requestBody.put("name", name)
            requestBody.put("address", address)
            requestBody.put("latitude", latitude)
            requestBody.put("longitude", longitude)
            requestBody.put("description", description)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val url = getString(R.string.apiplaces) + placeId
        val request = JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            { // Handle successful place update
                Toast.makeText(
                    this@EditPlaceActivity,
                    "Place updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        ) { // Handle error during place update
            Toast.makeText(
                this@EditPlaceActivity,
                "Error updating place",
                Toast.LENGTH_SHORT
            ).show()
        }
        Volley.newRequestQueue(this).add(request)
    }
}

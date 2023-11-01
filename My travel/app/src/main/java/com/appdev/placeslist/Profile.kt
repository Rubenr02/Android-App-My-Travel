@file:Suppress("DEPRECATION")

package com.appdev.placeslist


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class Profile : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username: String
    private var userId = 0

    private lateinit var placeList: MutableList<Place>
    private lateinit var adapter: PlacesAdapter2

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        placeList = mutableListOf()
        adapter = PlacesAdapter2(this, placeList)

        val recyclerView: RecyclerView = findViewById(R.id.display_user)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        sharedPreferences = applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("userid", 0)
        username = sharedPreferences.getString("username", "") ?: ""

        val loginInfotv: TextView = findViewById(R.id.displayusername)
        loginInfotv.text = "Username: $username"

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(this, UploadPlace::class.java))
}


        // Call a method to retrieve the places for the logged-in user
        getPlacesForUser(userId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPlacesForUser(userId: Int) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        // Construct the URL for retrieving places for the user
        val url = resources.getString(R.string.apidisplay) + "/$userId"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(i)
                        val place = Place(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            address = jsonObject.getString("address"),
                            latitude = jsonObject.getString("latitude"),
                            longitude = jsonObject.getString("longitude"),
                            description = jsonObject.getString("description"),
                            image = jsonObject.getString("image")
                            // ... Populate place properties from JSON data
                        )
                        placeList.add(place)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        progressDialog.dismiss()
                    }
                }
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()
            },
            { error ->
                Log.e("Volley", error.toString())
                progressDialog.dismiss()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonArrayRequest)
    }
}

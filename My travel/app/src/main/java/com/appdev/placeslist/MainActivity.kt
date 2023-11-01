package com.appdev.placeslist

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var list1: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var dividerItemDecoration: DividerItemDecoration
    private lateinit var placeList: MutableList<Place>
    private lateinit var filteredPlaceList: MutableList<Place>
    private lateinit var adapter: PlacesAdapter
    private lateinit var url: String
    private lateinit var sharedPreferences: SharedPreferences
    private var userId = 0
    private var username = ""

    private lateinit var searchView: SearchView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        url = resources.getString(R.string.apiplaces)

        list1 = findViewById(R.id.list1)
        searchView = findViewById(R.id.searchView)

        placeList = mutableListOf()
        filteredPlaceList = mutableListOf()
        adapter = PlacesAdapter(this, placeList)

        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dividerItemDecoration = DividerItemDecoration(list1.context, linearLayoutManager.orientation)

        list1.setHasFixedSize(true)
        list1.layoutManager = linearLayoutManager
        list1.addItemDecoration(dividerItemDecoration)
        list1.adapter = adapter

        getData()

        sharedPreferences = applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt("userid", 0)
        username = sharedPreferences.getString("username", "") ?: ""

        val loginInfo_tv: TextView = findViewById(R.id.loginInfo_tv)
        loginInfo_tv.text = "Username: $username"

        loginInfo_tv.setOnClickListener {
            sharedPreferences.edit().remove("userid").apply()
            finish()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                return true
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        val jsonArrayRequest = JsonArrayRequest(url,
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
                        )
                        place.description = place.latitude + ", " + place.longitude
                        placeList.add(place)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        progressDialog.dismiss()
                    }
                }
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()
            },
            { error: VolleyError ->
                Log.e("Volley", error.toString())
                progressDialog.dismiss()
            }
        )
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonArrayRequest)
    }

    private fun performSearch(query: String) {
        filteredPlaceList.clear()

        for (place in placeList) {
            if (place.name.contains(query, ignoreCase = true)) {
                filteredPlaceList.add(place)
            }
        }

        adapter.updateData(filteredPlaceList)
    }

    fun openProfileActivity(view: View) {
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
       }
}

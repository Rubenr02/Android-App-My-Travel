package com.appdev.placeslist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.appdev.placeslist.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import org.json.JSONException

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("idplace", 1)

        commentsRecyclerView = findViewById(R.id.display_comments)
        commentsAdapter = CommentsAdapter(this)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.adapter = commentsAdapter

        val placeId = intent.getIntExtra("placeId", 0) // Use 0 as the default value if no placeId is found
        getComments(placeId)

        getDetail(id)

        val openmaps = findViewById<Button>(R.id.openmaps)
        openmaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun getDetail(id: Int) {
        val url = resources.getString(R.string.url) + id + "/"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jresponse = response.getJSONObject(0)
                    jresponse.getInt("id")
                    val name = jresponse.getString("name")
                    val address = jresponse.getString("address")
                    val description = jresponse.getString("description")
                    val image = jresponse.getString("image")
                    val latitude = jresponse.getDouble("latitude")
                    val longitude = jresponse.getDouble("longitude")
                    val coordinates = "$latitude, $longitude"
                    val nameTv = findViewById<TextView>(R.id.Name_place)
                    nameTv.text = name
                    val addressTv = findViewById<TextView>(R.id.address_place)
                    addressTv.text = address
                    val descriptionTv = findViewById<TextView>(R.id.description_place)
                    descriptionTv.text = description
                    val coordinatesTv = findViewById<TextView>(R.id.coordinates_tv)
                    coordinatesTv.text = coordinates
                    val imageIv = findViewById<ImageView>(R.id.image_place)
                    Glide.with(this)
                        .load(image)
                        .into(imageIv)
                    msg(response.toString())
                } catch (je: JSONException) {
                    //
                }
            },
            {
                msg("That didn't work!")
            }
        )
        queue.add(jsonObjectRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun msg(txt: String) {
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show()
    }

    private fun getComments(placeId: Int) {
        val url = resources.getString(R.string.apicomment) + placeId

        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val comments = mutableListOf<Comment>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val comment = Comment(
                            idUser = jsonObject.getInt("idUser"),
                            idPlace = jsonObject.getInt("idplace"),
                            comment = jsonObject.getString("comment"),
                            userName = jsonObject.getString("userName")
                        )
                        comments.add(comment)
                    }
                    commentsAdapter.setData(comments)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            {
                // Handle error
            }
        )
        queue.add(jsonArrayRequest)
    }
}

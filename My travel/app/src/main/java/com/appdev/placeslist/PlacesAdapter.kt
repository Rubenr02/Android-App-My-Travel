package com.appdev.placeslist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject

class PlacesAdapter(private val context: Context, private var list: List<Place>) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = list[position]
        val idplace = place.id

        holder.nametv.text = place.name
        holder.addresstv.text = place.address
        holder.descriptiontv.text = place.description

        val userId = context.applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE).getInt("userid", 0)

        Glide.with(holder.imageiv.context)
            .load(place.image)
            .into(holder.imageiv)

        holder.likebtn.setOnClickListener {
            val jsonBody = JSONObject().apply {
                put("iduser", userId)
                put("idplace", idplace)
            }

            val urlpost = context.applicationContext.getString(R.string.apilike)

            val jsonobj = JsonObjectRequest(
                Request.Method.POST, urlpost, jsonBody,
                { response ->
                    val toast = Toast.makeText(context.applicationContext, response.toString(), Toast.LENGTH_LONG)
                    toast.show()
                },
                { error ->
                    val toast = Toast.makeText(context.applicationContext, error.toString(), Toast.LENGTH_LONG)
                    toast.show()
                }
            )

            val requestQueue = Volley.newRequestQueue(context.applicationContext)
            requestQueue.add(jsonobj)
        }

        holder.commentbtn.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra("idplace", idplace)
            context.startActivity(intent)
        }

        val itemClickListener = View.OnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("idplace", idplace)
            context.startActivity(intent)
        }

        holder.imageiv.setOnClickListener(itemClickListener)
        holder.nametv.setOnClickListener(itemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Place>) {
        list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nametv: TextView = itemView.findViewById(R.id.name_tv)
        val addresstv: TextView = itemView.findViewById(R.id.address_tv)
        val descriptiontv: TextView = itemView.findViewById(R.id.description_tv)
        val imageiv: ImageView = itemView.findViewById(R.id.image_iv)
        val likebtn: ImageView = itemView.findViewById(R.id.like_btn)
        val commentbtn: ImageView = itemView.findViewById(R.id.comment_btn)
    }
}
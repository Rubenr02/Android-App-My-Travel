package com.appdev.placeslist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class PlacesAdapter2(private val context: Context, private var list: MutableList<Place>) :
    RecyclerView.Adapter<PlacesAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_row2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = list[position]
        val idplace = place.id

        holder.nametv.text = place.name
        holder.addresstv.text = place.address
        holder.descriptiontv.text = place.description


        Glide.with(holder.imageiv.context)
            .load(place.image)
            .into(holder.imageiv)

        holder.editbtn.setOnClickListener {
            val intent = Intent(context, EditPlaceActivity::class.java)
            intent.putExtra("placeId", idplace.toString())
            context.startActivity(intent)
        }

        holder.deletebtn.setOnClickListener {
            val urlDelete =
                context.applicationContext.getString(R.string.apiplaces) + idplace

            val deleteRequest = object : JsonObjectRequest(
                Method.DELETE, urlDelete, null,
                Response.Listener {
                    // Handle successful deletion
                    Toast.makeText(
                        context.applicationContext,
                        "Place deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Remove the item from the list
                    list.removeAt(position)
                    // Notify the adapter that the item is removed at the specified position
                    notifyItemRemoved(position)
                    // Optionally, you can update the list after deletion
                    // list = list.filter { it.id != idplace }
                    // notifyDataSetChanged()
                },
                Response.ErrorListener { error ->
                    // Handle error during deletion
                    Toast.makeText(
                        context.applicationContext,
                        "Error deleting place: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    // Add any necessary headers for authentication or authorization
                    return headers
                }
            }

            val requestQueue = Volley.newRequestQueue(context.applicationContext)
            requestQueue.add(deleteRequest)
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


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nametv: TextView = itemView.findViewById(R.id.name_tv)
        val addresstv: TextView = itemView.findViewById(R.id.address_tv)
        val descriptiontv: TextView = itemView.findViewById(R.id.description_tv)
        val imageiv: ImageView = itemView.findViewById(R.id.image_iv)
        val editbtn: Button = itemView.findViewById(R.id.edit_btn)
        val deletebtn: Button = itemView.findViewById(R.id.delete_btn)
    }
}

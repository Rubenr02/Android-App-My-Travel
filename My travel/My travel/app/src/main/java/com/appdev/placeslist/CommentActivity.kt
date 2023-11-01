package com.appdev.placeslist


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CommentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val commentEditText: EditText = findViewById(R.id.comment_edit_text)
        val submitButton: Button = findViewById(R.id.submit_button)

        submitButton.setOnClickListener {
            val commentText = commentEditText.text.toString()
            val placeId = intent.getIntExtra("idplace", 0)
            submitCommentToDatabase(commentText, placeId)
        }
    }

    private fun submitCommentToDatabase(commentText: String, placeId: Int) {
        val userId = applicationContext.getSharedPreferences("app_preferences", MODE_PRIVATE).getInt("userid", 0)

        val jsonBody = JSONObject().apply {
            put("iduser", userId)
            put("idplace", placeId)
            put("comment", commentText)
        }

        val url = getString(R.string.apicomment)

        val jsonRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            {
                Toast.makeText(this, "Comment submitted successfully!", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Error submitting comment: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonRequest)
    }
}
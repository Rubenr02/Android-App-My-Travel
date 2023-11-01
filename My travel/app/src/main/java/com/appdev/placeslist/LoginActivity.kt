package com.appdev.placeslist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LoginActivity : AppCompatActivity() {
    private lateinit var logintxt: EditText
    private lateinit var passwordtxt: EditText
    private lateinit var loginbtn: Button
    private lateinit var registertv: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userid", 0)
        if (userId > 0) {
            after_login(userId, "")
        }

        logintxt = findViewById(R.id.editTextEmail)
        passwordtxt = findViewById(R.id.editTextPassword)
        registertv = findViewById(R.id.textViewSignUp)
        loginbtn = findViewById(R.id.buttonLogin)

        loginbtn.setOnClickListener {
            val user = logintxt.text.toString()
            val pass = passwordtxt.text.toString()
            var name = ""

            val parts = user.split("@")
            if (parts.size > 0) {
                name = parts[0]
            }

            var error = 0
            if (user.isEmpty()) {
                logintxt.error = "required"
                error = 1
            }
            if (pass.isEmpty()) {
                passwordtxt.error = "required"
                error = 1
            }

            if (error == 0) {
                do_login(user, pass, name)
            }
        }

        registertv.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun do_login(user: String, pass: String, name: String) {
        val url = applicationContext.resources.getString(R.string.apilogin)
        val urllogin = "$url/login/"

        val stringRequest = object : StringRequest(Method.POST, urllogin,
            Response.Listener<String> { response ->
                try {
                    val userId = response.toInt()
                    if (userId > 0)
                        after_login(userId, name)
                } catch (ex: NumberFormatException) {
                    val toast = Toast.makeText(applicationContext, response, Toast.LENGTH_LONG)
                    toast.show()
                }
            },
            Response.ErrorListener { error ->
                val toast = Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG)
                toast.show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = user
                params["pass"] = pass
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(stringRequest)
    }

    fun after_login(userId: Int, name: String) {
        with(sharedPreferences.edit()) {
            putInt("userid", userId)
            putString("username", name)
            apply()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

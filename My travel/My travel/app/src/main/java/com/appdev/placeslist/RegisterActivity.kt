package com.appdev.placeslist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RegisterActivity : AppCompatActivity() {
    private lateinit var login2txt: EditText
    private lateinit var password2txt: EditText
    private lateinit var password3txt: EditText
    private lateinit var email2txt: EditText
    private lateinit var registerbtn: Button
    private lateinit var logintv: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences = applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        login2txt = findViewById(R.id.login2_txt)
        email2txt = findViewById(R.id.email2_txt)
        password2txt = findViewById(R.id.password2_txt)
        password3txt = findViewById(R.id.password3_txt)
        logintv = findViewById(R.id.login_tv)
        registerbtn = findViewById(R.id.register_btn)

        registerbtn.setOnClickListener {
            val user = login2txt.text.toString()
            val pass2 = password2txt.text.toString()
            val pass3 = password3txt.text.toString()
            val email2 = email2txt.text.toString().trim()

            var error = 0
            if (user.isEmpty()) {login2txt.error = "required"; error = 1}
            if (pass2.isEmpty()) {password2txt.error = "required"; error = 1}
            if (pass3.isEmpty()) {password3txt.error = "required"; error = 1}
            if (email2.isEmpty()) {email2txt.error = "required"; error = 1}

            if (!Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
                email2txt.error = "Enter Valid Email Address"
                email2txt.requestFocus()
                error = 1
            }

            if (pass2 != pass3){
                password3txt.error = "passwords don't match!"
                password3txt.requestFocus()
                error = 1
            }

            if (error == 0) {
                do_register(user, pass2, email2)
            }
        }

        logintv.setOnClickListener {
            finish()
        }
    }

    private fun do_register(user: String, pass: String, email: String) {
        val url = applicationContext.resources.getString(R.string.apilogin)
        val urllogin = "$url/register/"

        val stringRequest = object : StringRequest(Method.POST, urllogin,
            Response.Listener { response ->
                try {
                    val userId = response.toInt()
                    if (userId > 0)
                        after_register(userId)
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
                params["email"] = email
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(stringRequest)
    }

    fun after_register(userId: Int) {
        with(sharedPreferences.edit()) {
            putInt("userid", userId)
            apply()
        }


        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

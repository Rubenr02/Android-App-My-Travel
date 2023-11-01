package com.appdev.placeslist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UploadPlace : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username: String
    private lateinit var placeNameEditText: EditText
    private lateinit var placeAddressEditText: EditText
    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var uploadImageButton: Button
    private var image: String = ""

    companion object {
        private const val REQUEST_IMAGE = 1
        private const val REQUEST_CAMERA = 2
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_place)

        sharedPreferences = applicationContext.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "") ?: ""
        val userId = sharedPreferences.getInt("userid", 0)

        val loginInfotv: TextView = findViewById(R.id.displayusername)
        loginInfotv.text = "Username: $username"

        placeNameEditText = findViewById(R.id.editTextPlaceName)
        placeAddressEditText = findViewById(R.id.editTextPlaceAddress)
        latitudeEditText = findViewById(R.id.editTextLatitude)
        longitudeEditText = findViewById(R.id.editTextLongitude)
        descriptionEditText = findViewById(R.id.editTextDescription)
        submitButton = findViewById(R.id.buttonSubmit)
        uploadImageButton = findViewById(R.id.buttonUploadImage)

        uploadImageButton.setOnClickListener {
            val options = arrayOf<CharSequence>("Choose from Gallery", "Take Photo", "Cancel")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Option")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Choose from Gallery" -> {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            openGallery()
                        } else {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                REQUEST_IMAGE
                            )
                        }
                    }
                    options[item] == "Take Photo" -> {
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            openCamera()
                        } else {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.CAMERA),
                                REQUEST_CAMERA
                            )
                        }
                    }
                    options[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        submitButton.setOnClickListener {
            val placeName = placeNameEditText.text.toString().trim()
            val placeAddress = placeAddressEditText.text.toString().trim()
            val latitude = latitudeEditText.text.toString().trim()
            val longitude = longitudeEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (placeName.isEmpty() || placeAddress.isEmpty() || latitude.isEmpty() ||
                longitude.isEmpty() || description.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            } else {
                insertPlace(placeName, placeAddress, latitude, longitude, description, userId, image)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            val selectedImage: Uri? = data?.data
            image = selectedImage?.toString() ?: ""
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val capturedImage: Bitmap? = data?.extras?.get("data") as Bitmap
            image = getImageUriAsString(capturedImage)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun insertPlace(
        name: String,
        address: String,
        latitude: String,
        longitude: String,
        description: String,
        iduser: Int,
        image: String
    ) {
        val url = getString(R.string.apiplaces)

        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("address", address)
        jsonObject.put("latitude", latitude)
        jsonObject.put("longitude", longitude)
        jsonObject.put("description", description)
        jsonObject.put("iduser", iduser)


        val requestBody = jsonObject.toString()

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                clearInputFields()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun clearInputFields() {
        placeNameEditText.text.clear()
        placeAddressEditText.text.clear()
        latitudeEditText.text.clear()
        longitudeEditText.text.clear()
        descriptionEditText.text.clear()
    }

    private fun getImageUriAsString(bitmap: Bitmap?): String {
        val tempDir: File = applicationContext.externalCacheDir ?: applicationContext.cacheDir
        val tempFile = File(tempDir, "temp_image.jpg")

        try {
            val outputStream = FileOutputStream(tempFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return tempFile.toURI().toString()
    }

}

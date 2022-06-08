package com.example.recyclerviewimplement


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.*
import com.auth0.android.jwt.JWT
import org.json.JSONObject


class DisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displau)
        val apiTokenResponse = findViewById<TextView>(R.id.getAPIResponse)

        val bundle = intent.extras
        if (bundle != null) {
            apiTokenResponse.text = "${bundle.getString("anushka")}"

        }

        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sharedPreferences.getString("access-token", null)
        if (token.isNullOrEmpty()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            return
        }
        val presentTime = System.currentTimeMillis() / 1000
        val jwtToken: JWT = JWT(token)
        val expiryTime: String? = jwtToken.getClaim("exp").asString()
        val timeLeftToExpire = ((expiryTime?.toInt())?.minus((presentTime.toInt())))
        val timeToExpireInMinutes = timeLeftToExpire?.div(60) //TimeInMinutes
        if (timeToExpireInMinutes!! >= 5) {
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
            return
        }
        val queue = Volley.newRequestQueue(this)
        val url = "https://api-smartflo.tatateleservices.com/v1/auth/refresh"
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(
                    this,
                    "Your token will be refreshed when there will be less than 5 minutes left",
                    Toast.LENGTH_SHORT
                ).show()
                val jsonObject = JSONObject(response)
                val refreshToken = jsonObject.getString("access_token")
                Toast.makeText(this, "Refresh Token", Toast.LENGTH_SHORT).show()
                val myEdit = sharedPreferences.edit()
                myEdit.putString("access-token", refreshToken)
                myEdit.apply()
                myEdit.commit()


            },
            Response.ErrorListener {
                apiTokenResponse.text = "That didn't work!"
            }) {

            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer $token")
                return headers
            }

        }
        queue.add(stringRequest)
    }
}
package com.example.recyclerviewimplement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.HashMap

class APIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apiactivity)
//        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
//        val token = sharedPreferences.getString("access-token", null)
////Broadcast API
//        val queue = Volley.newRequestQueue(this)
//        val url = "https://cloudphone.tatateleservices.com/api/v2/broadcasts"
//        val stringRequest = object : StringRequest(
//            Request.Method.GET, url,
//            Response.Listener<String> { response ->
//                val jsonObject = JSONObject(response)
//                val limit = jsonObject.getInt("limit")
//                val data = ArrayList<ItemsViewModel>()
//                val jsonArray = jsonObject.getJSONArray("results")
//                for(i in 0 until jsonArray.length() )
//                {
//                    val apiObject = jsonArray.getJSONObject(i)
//                    val api=ItemsViewModel(
//                        apiObject.getString("name"),
//                        apiObject.getString("description")
//                    )
//                    data.add(api)
//                }
//            },
//            Response.ErrorListener {
//
//            }) {
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String, String>()
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer $token")
//                return headers
//            }
//        }
//        queue.add(stringRequest)

    }

    }
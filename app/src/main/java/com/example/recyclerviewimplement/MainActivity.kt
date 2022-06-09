package com.example.recyclerviewimplement

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.net.ConnectException
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    val data = ArrayList<ItemsViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var adapter: CustomAdapter? = null
    var page = 1
    var limit = 3
    val recordsThreshold = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)
        if (token.isNullOrEmpty()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            return
        }

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        fetchData(token, page)

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                val totalCount = linearLayoutManager.itemCount
                val lastVisibleITem = linearLayoutManager.findLastVisibleItemPosition()
                if (totalCount <= lastVisibleITem + recordsThreshold) {
                    page++
                    fetchData(token, page)
                }
            }
        })
    }

    private fun fetchData(token: String?, page: Int) {
        if (page > limit) {
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show()
            progressBar!!.visibility = View.GONE
            return
        }
        // broadcast API
        val queue = Volley.newRequestQueue(this)
        val url = "https://cloudphone.tatateleservices.com/api/v2/broadcasts?page=$page&limit=15"
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val jsonObject = JSONObject(response)
                var size = jsonObject.getString("size")
                var presentPage = jsonObject.getString("page")
                val jsonArray = jsonObject.getJSONArray("results")
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                recyclerView.layoutManager = LinearLayoutManager(this)

                for (i in 0 until jsonArray.length()) {
                    progressBar.visibility = View.INVISIBLE
                    val apiObject = jsonArray.getJSONObject(i)
                    val name = apiObject.getString("name")
                    val description = apiObject.getString("description")
                    data.add(ItemsViewModel("Name : $name", "desc. : $description"))
                }
                val adapter = CustomAdapter(data)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

            },
            Response.ErrorListener { error ->
                val msg = getVolleyError(error)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer $token")
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun getVolleyError(error: VolleyError): String? {
        var errorMsg = ""
        if (error is NoConnectionError) {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            activeNetwork = cm.activeNetworkInfo
            errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                "Server is not connected to the internet. Please try again"
            } else {
                "Your device is not connected to internet.please try again with active internet connection"
            }
        } else if (error is NetworkError || error.cause is ConnectException) {
            errorMsg =
                "Your device is not connected to internet.please try again with active internet connection"
        } else {
            errorMsg = "Unable to validate token"
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        return errorMsg
    }

}

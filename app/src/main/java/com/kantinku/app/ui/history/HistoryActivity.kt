package com.kantinku.app.ui.history

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.adapter.HistoryAdapter
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.OrderData
import com.kantinku.app.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        findViewById<View>(R.id.btn_back).setOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rv_history)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(emptyList())
        rv.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        val token = SessionManager.getInstance(this).fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val tvEmpty = findViewById<TextView>(R.id.tv_empty)
        progressBar.visibility = View.VISIBLE

        val apiService = ApiClient.client.create(ApiService::class.java)
        apiService.getOrders("Bearer $token").enqueue(object : Callback<List<OrderData>> {
            override fun onResponse(call: Call<List<OrderData>>, response: Response<List<OrderData>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    if (orders.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        adapter.updateData(orders)
                    }
                } else {
                    Toast.makeText(this@HistoryActivity, "Gagal memuat riwayat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@HistoryActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
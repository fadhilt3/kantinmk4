package com.kantinku.app.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.adapter.FoodAdapter
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.Menu
import com.kantinku.app.model.FoodItem
import com.kantinku.app.model.toFoodItem
import com.kantinku.app.session.SessionManager
import com.kantinku.app.utils.CartManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteActivity : AppCompatActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var tvCount: TextView
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        rvFavorites = findViewById(R.id.rv_favorites)
        layoutEmpty = findViewById(R.id.layout_empty)
        tvCount = findViewById(R.id.tv_count)

        findViewById<TextView>(R.id.btn_back).setOnClickListener { finish() }

        rvFavorites.layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(mutableListOf(), object : FoodAdapter.OnAddListener {
            override fun onAdd(item: FoodItem?) {
                item?.let {
                    CartManager.instance.addItem(it)
                    Toast.makeText(this@FavoriteActivity, "${it.name} ditambahkan!", Toast.LENGTH_SHORT).show()
                }
            }
        })
        rvFavorites.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val token = SessionManager.getInstance(this).fetchAuthToken() ?: return

        val apiService = ApiClient.client.create(ApiService::class.java)
        apiService.getFavorites("Bearer $token").enqueue(object : Callback<List<Menu>> {
            override fun onResponse(call: Call<List<Menu>>, response: Response<List<Menu>>) {
                if (response.isSuccessful) {
                    val menus = response.body() ?: emptyList()
                    val foodItems = menus.map { it.toFoodItem().apply { isFavorite = true } }

                    if (foodItems.isEmpty()) {
                        layoutEmpty.visibility = View.VISIBLE
                        rvFavorites.visibility = View.GONE
                        tvCount.text = "0 menu"
                    } else {
                        layoutEmpty.visibility = View.GONE
                        rvFavorites.visibility = View.VISIBLE
                        tvCount.text = "${foodItems.size} menu"
                        adapter.updateData(foodItems)
                    }
                }
            }

            override fun onFailure(call: Call<List<Menu>>, t: Throwable) {
                Toast.makeText(this@FavoriteActivity, "Gagal load favorit", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
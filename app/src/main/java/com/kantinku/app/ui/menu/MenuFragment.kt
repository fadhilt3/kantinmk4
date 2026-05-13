package com.kantinku.app.ui.menu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.adapter.FoodAdapter
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.FoodItem
import com.kantinku.app.model.Menu
import com.kantinku.app.model.toFoodItem
import com.kantinku.app.ui.home.MainActivity
import com.kantinku.app.utils.CartManager
import com.kantinku.app.utils.DataProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment() {
    private var adapter: FoodAdapter? = null
    private var activeChip: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val rv = v.findViewById<RecyclerView>(R.id.rv_menu)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = FoodAdapter(mutableListOf(), object : FoodAdapter.OnAddListener {
            override fun onAdd(food: FoodItem?) {
                food?.let { addToCart(it) }
            }
        })
        rv.adapter = adapter

        // Search
        val etSearch = v.findViewById<EditText>(R.id.et_search)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {
                adapter?.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Setup category chips
        setupCategoryChips(v)

        // Load menu dari API
        loadMenuFromApi()
    }

    private fun loadMenuFromApi() {
        val apiService = ApiClient.client.create(ApiService::class.java)
        apiService.getMenu().enqueue(object : Callback<List<Menu?>> {
            override fun onResponse(call: Call<List<Menu?>>, response: Response<List<Menu?>>) {
                if (response.isSuccessful) {
                    val menus = response.body()?.filterNotNull() ?: emptyList()
                    val foodItems = menus.map { it.toFoodItem() }
                    adapter?.updateData(foodItems)
                } else {
                    loadDummyData()
                }
            }

            override fun onFailure(call: Call<List<Menu?>>, t: Throwable) {
                loadDummyData()
                Toast.makeText(requireContext(), "Menggunakan data lokal", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDummyData() {
        adapter?.updateData(DataProvider.allMenu)
    }

    private fun setupCategoryChips(v: View) {
        val catContainer = v.findViewById<LinearLayout>(R.id.category_filter)
        val cats = DataProvider.categories
        val labels = arrayOf("Semua", "Nasi", "Mie", "Kuah", "Lauk", "Sayur", "Minuman", "Snack")

        for (i in cats.indices) {
            val cat = cats[i]
            val chip = TextView(requireContext())
            chip.text = labels[i]
            chip.textSize = 12f
            chip.setPadding(40, 0, 40, 0)
            chip.height = 88
            chip.gravity = Gravity.CENTER

            if (i == 0) {
                chip.setBackgroundResource(R.drawable.bg_chip_active)
                chip.setTextColor(requireContext().getColor(R.color.text_white))
                activeChip = chip
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip_default)
                chip.setTextColor(requireContext().getColor(R.color.text_secondary))
            }

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.marginEnd = 16
            chip.layoutParams = lp

            chip.setOnClickListener {
                activeChip?.let {
                    it.setBackgroundResource(R.drawable.bg_chip_default)
                    it.setTextColor(requireContext().getColor(R.color.text_secondary))
                }
                chip.setBackgroundResource(R.drawable.bg_chip_active)
                chip.setTextColor(requireContext().getColor(R.color.text_white))
                activeChip = chip
                adapter?.filterByCategory(cat)
            }
            catContainer.addView(chip)
        }
    }

    private fun addToCart(food: FoodItem) {
        CartManager.instance.addItem(food)
        (activity as? MainActivity)?.updateBadge()
        Toast.makeText(requireContext(), "${food.name} ditambahkan!", Toast.LENGTH_SHORT).show()
    }
}
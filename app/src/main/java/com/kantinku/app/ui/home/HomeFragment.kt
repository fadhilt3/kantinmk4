package com.kantinku.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kantinku.app.R
import com.kantinku.app.adapter.FoodAdapter
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.FoodItem
import com.kantinku.app.model.Menu
import com.kantinku.app.model.toFoodItem
import com.kantinku.app.session.SessionManager
import com.kantinku.app.ui.notification.NotificationActivity
import com.kantinku.app.utils.CartManager
import com.kantinku.app.utils.DataProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.kantinku.app.ui.favorite.FavoriteActivity
class HomeFragment : Fragment() {

    private var adapter: FoodAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        // Session
        val sm = SessionManager.getInstance(requireContext())
        v.findViewById<TextView>(R.id.tv_greeting).text = "Halo, ${sm.firstName} 👋"
        v.findViewById<TextView>(R.id.tv_username).text = "Mau makan apa hari ini?"
        v.findViewById<TextView>(R.id.tv_avatar).text = sm.avatar

        // Favorite
        v.findViewById<View>(R.id.btn_favorite).setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteActivity::class.java))
        }

        // RecyclerView menu
        val rv = v.findViewById<RecyclerView>(R.id.rv_menu)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = FoodAdapter(mutableListOf(), object : FoodAdapter.OnAddListener {
            override fun onAdd(food: FoodItem?) {
                food?.let { addToCart(it) }
            }
        })
        rv.adapter = adapter

        // Category chips
        val catContainer = v.findViewById<LinearLayout>(R.id.category_container)
        val cats = DataProvider.categories
        val catLabels = arrayOf(
            "✨ Semua", "🍙 Nasi", "🍜 Mie",
            "🥣 Kuah", "🍗 Lauk", "🥗 Sayur",
            "🍹 Minuman", "🍡 Snack"
        )

        var activeChip: TextView? = null
        for (i in cats.indices) {
            val cat = cats[i]
            val chip = TextView(requireContext())
            chip.text = catLabels[i]
            chip.textSize = 12f
            chip.setPadding(40, 0, 40, 0)
            chip.height = 92
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

        // Search
        val etSearch = v.findViewById<EditText>(R.id.et_search)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter?.filter(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // See All
        v.findViewById<View>(R.id.tv_see_all).setOnClickListener {
            (activity as? MainActivity)?.goMenu()
        }

        // Load menu dari API
        loadMenuFromApi(v)
    }

    private fun loadMenuFromApi(v: View) {
        val apiService = ApiClient.client.create(ApiService::class.java)
        apiService.getMenu().enqueue(object : Callback<List<Menu?>> {
            override fun onResponse(call: Call<List<Menu?>>, response: Response<List<Menu?>>) {
                if (response.isSuccessful) {
                    val menus = response.body()?.filterNotNull() ?: emptyList()
                    val foodItems = menus.map { it.toFoodItem() }
                    adapter?.updateData(foodItems)
                    setupPopular(v, foodItems)
                } else {
                    loadDummyData(v)
                }
            }

            override fun onFailure(call: Call<List<Menu?>>, t: Throwable) {
                loadDummyData(v)
            }
        })
    }

    private fun setupPopular(v: View, foodItems: List<FoodItem>) {
        val popularContainer = v.findViewById<LinearLayout>(R.id.popular_container)
        popularContainer.removeAllViews()

        val popularItems = foodItems.filter { it.isPopular }
        val itemsToShow = if (popularItems.isEmpty()) foodItems.take(5) else popularItems

        for (food in itemsToShow) {
            val card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_food_popular, popularContainer, false)

            val imgFood = card.findViewById<ImageView>(R.id.img_food)
            val layoutEmoji = card.findViewById<LinearLayout>(R.id.layout_emoji)
            val tvEmoji = card.findViewById<TextView>(R.id.tv_emoji)

            if (!food.foto.isNullOrEmpty()) {
                imgFood.visibility = View.VISIBLE
                layoutEmoji.visibility = View.GONE
                Glide.with(requireContext())
                    .load(food.foto)
                    .centerCrop()
                    .into(imgFood)
            } else {
                imgFood.visibility = View.GONE
                layoutEmoji.visibility = View.VISIBLE
                tvEmoji.text = food.emoji
            }

            card.findViewById<TextView>(R.id.tv_name).text = food.name
            card.findViewById<TextView>(R.id.tv_rating).text = String.format("%.1f", food.rating)
            val ribuan = food.price / 1000
            card.findViewById<TextView>(R.id.tv_price).text = "Rp ${ribuan}k"
            card.findViewById<View>(R.id.btn_add).setOnClickListener { addToCart(food) }
            card.setOnClickListener { addToCart(food) }

            popularContainer.addView(card)
        }
    }

    private fun loadDummyData(v: View) {
        val allMenu = DataProvider.allMenu
        adapter?.updateData(allMenu.toMutableList())
        setupPopularDummy(v, allMenu)
    }

    private fun setupPopularDummy(v: View, allMenu: List<FoodItem>) {
        val popularContainer = v.findViewById<LinearLayout>(R.id.popular_container)
        popularContainer.removeAllViews()

        for (food in allMenu) {
            if (!food.isPopular) continue
            val card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_food_popular, popularContainer, false)

            val layoutEmoji = card.findViewById<LinearLayout>(R.id.layout_emoji)
            val tvEmoji = card.findViewById<TextView>(R.id.tv_emoji)
            val imgFood = card.findViewById<ImageView>(R.id.img_food)

            imgFood.visibility = View.GONE
            layoutEmoji.visibility = View.VISIBLE
            tvEmoji.text = food.emoji

            card.findViewById<TextView>(R.id.tv_name).text = food.name
            card.findViewById<TextView>(R.id.tv_rating).text = String.format("%.1f", food.rating)
            val ribuan = food.price / 1000
            card.findViewById<TextView>(R.id.tv_price).text = "Rp ${ribuan}k"
            card.findViewById<View>(R.id.btn_add).setOnClickListener { addToCart(food) }
            card.setOnClickListener { addToCart(food) }

            popularContainer.addView(card)
        }
    }

    private fun addToCart(food: FoodItem) {
        CartManager.instance.addItem(food)
        (activity as? MainActivity)?.updateBadge()
        Toast.makeText(requireContext(), "${food.name} ditambahkan! 🛒", Toast.LENGTH_SHORT).show()
    }
}
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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.adapter.FoodAdapter
import com.kantinku.app.model.FoodItem
import com.kantinku.app.session.SessionManager
import com.kantinku.app.ui.notification.NotificationActivity
import com.kantinku.app.utils.CartManager
import com.kantinku.app.utils.DataProvider

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        // -- Session --
        val sm = SessionManager.getInstance(requireContext())
        v.findViewById<TextView>(R.id.tv_greeting).text = "Halo, ${sm.firstName} 👋"
        v.findViewById<TextView>(R.id.tv_username).text = "Mau makan apa hari ini?"
        v.findViewById<TextView>(R.id.tv_avatar).text = sm.avatar

        // -- Notifikasi --
        v.findViewById<View>(R.id.btn_notif).setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }

        // -- Data --
        val allMenu = DataProvider.getAllMenu()

        // -- Popular scroll --
        val popularContainer = v.findViewById<LinearLayout>(R.id.popular_container)
        for (food in allMenu) {
            if (!food.isPopular) continue
            val card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_food_popular, popularContainer, false)

            card.findViewById<TextView>(R.id.tv_emoji).text = food.emoji
            card.findViewById<TextView>(R.id.tv_name).text = food.name
            card.findViewById<TextView>(R.id.tv_rating).text = String.format("%.1f", food.rating)

            val ribuan = food.price / 1000
            card.findViewById<TextView>(R.id.tv_price).text = "Rp ${ribuan}k"

            card.findViewById<View>(R.id.btn_add).setOnClickListener { addToCart(food) }
            card.setOnClickListener { addToCart(food) }

            popularContainer.addView(card)
        }

        // -- Category chips --
        val catContainer = v.findViewById<LinearLayout>(R.id.category_container)
        val cats = DataProvider.getCategories()
        val catLabels = arrayOf(
            "✨ Semua", "🍙 Nasi", "🍜 Mie",
            "🥣 Kuah", "🍗 Lauk", "🥗 Sayur",
            "🍹 Minuman", "🍡 Snack"
        )

        // -- RecyclerView --
        val rv = v.findViewById<RecyclerView>(R.id.rv_menu)
        rv.layoutManager = LinearLayoutManager(requireContext())

        val adapter = FoodAdapter(allMenu, object : FoodAdapter.OnAddListener {
            override fun onAdd(food: FoodItem?) {
                food?.let { addToCart(it) }
            }
        })
        rv.adapter = adapter

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
                adapter.filterByCategory(cat)
            }
            catContainer.addView(chip)
        }

        // -- Search --
        val etSearch = v.findViewById<EditText>(R.id.et_search)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // -- See All --
        v.findViewById<View>(R.id.tv_see_all).setOnClickListener {
            (activity as? MainActivity)?.goMenu()
        }
    }

    private fun addToCart(food: FoodItem) {
        CartManager.getInstance().addItem(food)
        (activity as? MainActivity)?.updateBadge()
        Toast.makeText(requireContext(), "${food.name} ditambahkan! 🛒", Toast.LENGTH_SHORT).show()
    }
}
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
import com.kantinku.app.model.FoodItem
import com.kantinku.app.ui.home.MainActivity
import com.kantinku.app.utils.CartManager
import com.kantinku.app.utils.DataProvider

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

        // Gunakan nama fungsi asli dari Java
        val allMenu = DataProvider.getAllMenu()
        val rv = v.findViewById<RecyclerView>(R.id.rv_menu)
        rv.layoutManager = LinearLayoutManager(requireContext())

        // Memperbaiki Error "OnAddListener was expected"
        adapter = FoodAdapter(allMenu, object : FoodAdapter.OnAddListener {
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

        // Category chips
        val catContainer = v.findViewById<LinearLayout>(R.id.category_filter)
        val cats = DataProvider.getCategories()
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
        // Pastikan getInstance() terpanggil dengan benar
        CartManager.getInstance().addItem(food)
        (activity as? MainActivity)?.updateBadge()
        Toast.makeText(requireContext(), "${food.name} ditambahkan!", Toast.LENGTH_SHORT).show()
    }
}
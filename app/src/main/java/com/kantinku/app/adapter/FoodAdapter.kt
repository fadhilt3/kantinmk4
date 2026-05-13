package com.kantinku.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kantinku.app.R
import com.kantinku.app.model.FoodItem
import com.kantinku.app.utils.CartManager
import java.util.Locale

class FoodAdapter(private val allItems: MutableList<FoodItem>, private val listener: OnAddListener) :
    RecyclerView.Adapter<FoodAdapter.VH>() {
    private val filtered: MutableList<FoodItem> = ArrayList(allItems)

    interface OnAddListener {
        fun onAdd(item: FoodItem?)
    }

    fun updateData(newItems: List<FoodItem>) {
        allItems.clear()
        allItems.addAll(newItems)
        filtered.clear()
        filtered.addAll(newItems)
        notifyDataSetChanged()
    }

    fun filter(query: String?) {
        filtered.clear()
        if (query == null || query.isEmpty()) {
            filtered.addAll(allItems)
        } else {
            val q = query.lowercase(Locale.getDefault())
            for (f in allItems) if (f.name.lowercase(Locale.getDefault())
                    .contains(q) || f.kantin.lowercase(Locale.getDefault()).contains(q)
            ) filtered.add(f)
        }
        notifyDataSetChanged()
    }

    fun filterByCategory(cat: String) {
        filtered.clear()
        if (cat == "semua") {
            filtered.addAll(allItems)
        } else {
            for (f in allItems) if (f.category == cat) filtered.add(f)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food_card, parent, false)
        )
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val f = filtered[pos]
        h.emoji.text = f.emoji
        h.name.text = f.name
        h.kantin.text = f.kantin
        h.price.text = CartManager.rupiah(f.price)
        h.rating.text = String.format("%.1f", f.rating)
        h.review.text = "(" + f.reviewCount + ")"
        if (f.isPopular) {
            h.badge.visibility = View.VISIBLE
            h.badge.text = "🔥 Hits"
        } else if (f.isNew) {
            h.badge.visibility = View.VISIBLE
            h.badge.text = "🆕 Baru"
        } else h.badge.visibility = View.GONE
        h.btnAdd.setOnClickListener { v: View? -> listener.onAdd(f) }
        h.itemView.setOnClickListener { v: View? -> listener.onAdd(f) }
    }

    override fun getItemCount(): Int {
        return filtered.size
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        var emoji: TextView = v.findViewById(R.id.tv_emoji)
        var name: TextView = v.findViewById(R.id.tv_name)
        var kantin: TextView = v.findViewById(R.id.tv_kantin)
        var price: TextView = v.findViewById(R.id.tv_price)
        var rating: TextView = v.findViewById(R.id.tv_rating)
        var review: TextView = v.findViewById(R.id.tv_review)
        var badge: TextView = v.findViewById(R.id.tv_badge)
        var btnAdd: TextView = v.findViewById(R.id.btn_add)
    }
}
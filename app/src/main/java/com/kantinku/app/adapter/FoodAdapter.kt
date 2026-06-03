package com.kantinku.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kantinku.app.R
import com.kantinku.app.api.ApiClient
import com.kantinku.app.api.ApiService
import com.kantinku.app.model.FavoriteResponse
import com.kantinku.app.model.FoodItem
import com.kantinku.app.session.SessionManager
import com.kantinku.app.utils.CartManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        h.name.text = f.name
        h.kantin.text = f.kantin
        h.price.text = CartManager.rupiah(f.price)
        h.rating.text = String.format("%.1f", f.rating)
        h.review.text = "(" + f.reviewCount + ")"

        // Load foto atau emoji
        if (!f.foto.isNullOrEmpty()) {
            h.imgFood.visibility = View.VISIBLE
            h.layoutEmoji.visibility = View.GONE
            Glide.with(h.itemView.context)
                .load(f.foto)
                .placeholder(R.drawable.bg_input)
                .error(R.drawable.bg_input)
                .centerCrop()
                .into(h.imgFood)
        } else {
            h.imgFood.visibility = View.GONE
            h.layoutEmoji.visibility = View.VISIBLE
            h.emoji.text = f.emoji
        }

        if (f.isPopular) {
            h.badge.visibility = View.VISIBLE
            h.badge.text = "🔥 Hits"
        } else if (f.isNew) {
            h.badge.visibility = View.VISIBLE
            h.badge.text = "🆕 Baru"
        } else h.badge.visibility = View.GONE

        // Set icon favorit
        h.btnFavorite.setImageResource(
            if (f.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )

        // Toggle favorit
        h.btnFavorite.setOnClickListener {
            val context = h.itemView.context
            val token = SessionManager.getInstance(context).fetchAuthToken() ?: return@setOnClickListener

            val apiService = ApiClient.client.create(ApiService::class.java)
            apiService.toggleFavorite("Bearer $token", f.id.toInt())
                .enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(call: Call<FavoriteResponse>, response: Response<FavoriteResponse>) {
                        if (response.isSuccessful) {
                            val status = response.body()?.status
                            f.isFavorite = status == "added"
                            h.btnFavorite.setImageResource(
                                if (f.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                            )
                            val msg = if (f.isFavorite) "Ditambahkan ke favorit" else "Dihapus dari favorit"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        Toast.makeText(context, "Gagal update favorit", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        h.btnAdd.setOnClickListener { listener.onAdd(f) }
        h.itemView.setOnClickListener { listener.onAdd(f) }
    }

    override fun getItemCount(): Int = filtered.size

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        var imgFood: ImageView = v.findViewById(R.id.img_food)
        var layoutEmoji: LinearLayout = v.findViewById(R.id.layout_emoji)
        var emoji: TextView = v.findViewById(R.id.tv_emoji)
        var name: TextView = v.findViewById(R.id.tv_name)
        var kantin: TextView = v.findViewById(R.id.tv_kantin)
        var price: TextView = v.findViewById(R.id.tv_price)
        var rating: TextView = v.findViewById(R.id.tv_rating)
        var review: TextView = v.findViewById(R.id.tv_review)
        var badge: TextView = v.findViewById(R.id.tv_badge)
        var btnFavorite: ImageView = v.findViewById(R.id.btn_favorite)
        var btnAdd: TextView = v.findViewById(R.id.btn_add)
    }
}